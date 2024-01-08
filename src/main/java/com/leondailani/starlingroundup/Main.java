package com.leondailani.starlingroundup;

import com.leondailani.starlingroundup.api.StarlingClient;
import com.leondailani.starlingroundup.models.*;
import com.leondailani.starlingroundup.utils.AccessTokenLoader;
import com.leondailani.starlingroundup.utils.RoundUpCalculator;
import spark.Response;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.DayOfWeek;

import static com.leondailani.starlingroundup.utils.Hasher.*;
import static spark.Spark.*;
/**
 * The main method takes all the transactions over the past week for a Starling Bank customer's
 * first account. It then calculates the round-up. It then creates a savings goal with a name and
 * target amount that can be adjusted accordingly. It then transfers funds from the round-up into
 * the savings goal accounting for whether these funds exist in the account or not (whether there
 * is enough money in the account to make the transfer).
 */
public class Main {
    public static void main(String[] args) {
        port(8080); // Set the port for the embedded server (optional, default is 4567)

        post("/roundup", (request, response) -> {
            // Parse and validate input parameters
            String accessToken = request.queryParams("access_token");
            String savingsGoalName = request.queryParams("savings_goal_name");
            String savingsGoalAmountString = request.queryParams("savings_goal_amount");
            int savingsGoalAmount = 0;

            if (accessToken == null || accessToken.isEmpty() ||
                    savingsGoalName == null || savingsGoalName.isEmpty() ||
                    savingsGoalAmountString == null || savingsGoalAmountString.isEmpty()) {
                response.status(400); // Bad request if any parameter is missing
                return "Access token, savings goal name, and savings goal amount are required.";
            }

            try {
                savingsGoalAmount = Integer.parseInt(savingsGoalAmountString);
            } catch (NumberFormatException e) {
                response.status(400); // Bad request if amount is not a valid integer
                return "Savings goal amount must be a valid integer.";
            }

            // Execute the round-up functionality in a separate method for better code organization
            return executeRoundUp(accessToken, savingsGoalName, savingsGoalAmount, response);
        });
    }

    private static String executeRoundUp(String accessToken, String savingsGoalName, int savingsGoalAmount, Response response) {
        try {
            StarlingClient client = new StarlingClient(accessToken);

            // Retrieve account details
            ClientAccounts accounts = client.getAccounts();
            if (accounts == null || accounts.getAccounts().isEmpty()) {
                throw new IllegalArgumentException("No accounts available.");
            }

            // Get the first account's details
            Account account = accounts.getAccounts().get(0);
            String accountUid = account.getAccountUid();
            String categoryUid = account.getDefaultCategory();

            // Determine the start and end of the current week
            ZonedDateTime startOfWeek = ZonedDateTime.now().with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
            ZonedDateTime endOfWeek = startOfWeek.plusDays(7);

            // Fetch transactions for the specified week
            FeedItems transactionDetails = client.getWeeklyTransactions(accountUid, categoryUid, startOfWeek, endOfWeek);
            if (transactionDetails == null || transactionDetails.getFeedItems().isEmpty()) {
                throw new IllegalArgumentException("No transaction data available for the specified week.");
            }

            String hash = generateHash(accountUid, startOfWeek, endOfWeek);
            String hashFilePath = "src/main/resources/hashes.txt"; // The file where hashes are stored

            // Check if this hash already exists
            if (checkIfHashExists(hash, hashFilePath)) {
                return "A savings goal for this timeframe already exists.";
            }

            // Calculate the round-up amount from the week's transactions
            int roundUpAmount = RoundUpCalculator.calculateTotalRoundUp(transactionDetails.getFeedItems());
            if (roundUpAmount <= 0) {
                return "No round-up amount calculated for the current week.";
            }

            // Create a savings goal
            SavingsGoalRequest goalRequest = new SavingsGoalRequest(savingsGoalName, "GBP", savingsGoalAmount);
            SavingsGoal savingsGoalResponse = client.createSavingsGoal(accountUid, goalRequest);
            if (savingsGoalResponse == null || !savingsGoalResponse.getSuccess()) {
                throw new Exception("Failed to create the savings goal.");
            }

            // Transfer the round-up amount to the savings goal
            String savingsGoalUid = savingsGoalResponse.getSavingsGoalUid();
            SavingsGoalTransfer transferResponse = client.addMoneyToSavingsGoal(accountUid, savingsGoalUid, roundUpAmount);
            if (transferResponse == null || !transferResponse.getSuccess()) {
                throw new Exception("Failed to transfer funds to the savings goal.");
            }

            storeHash(hash,hashFilePath);

            // Confirmation message
            return String.format("Savings Goal with UID (%s) of %s pence successfully created for account with UID (%s), and %s pence deposited into the goal from the past week's transactions.",
                    savingsGoalUid, savingsGoalAmount, accountUid, roundUpAmount);

        } catch (IllegalArgumentException e) {
            response.status(400); // Bad request for illegal argument
            return e.getMessage();
        } catch (Exception e) {
            response.status(500); // Internal server error for unhandled exceptions
            return "An error occurred: " + e.getMessage();
        }
    }
}