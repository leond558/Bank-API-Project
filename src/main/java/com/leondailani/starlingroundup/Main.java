package com.leondailani.starlingroundup;

import static com.leondailani.starlingroundup.utils.RoundUpExecutor.executeRoundUp;
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
            String accountIndexString = request.queryParams("account_index");
            int savingsGoalAmount;
            int accountIndex;

            if (accessToken == null || accessToken.isEmpty() ||
                    savingsGoalName == null || savingsGoalName.isEmpty() ||
                    savingsGoalAmountString == null || savingsGoalAmountString.isEmpty() ||
                    accountIndexString == null || accountIndexString.isEmpty()){
                response.status(400); // Bad request if any parameter is missing
                return "Access token, savings goal name, and savings goal amount are required.";
            }

            try {
                savingsGoalAmount = Integer.parseInt(savingsGoalAmountString);
            } catch (NumberFormatException e) {
                response.status(400); // Bad request if amount is not a valid integer
                return "Savings goal amount must be a valid integer.";
            }

            try {
                accountIndex = Integer.parseInt(savingsGoalAmountString);
            } catch (NumberFormatException e) {
                response.status(400); // Bad request if amount is not a valid integer
                return "Account index number must be a valid integer.";
            }

            // Execute the round-up functionality in a separate method for better code organization
            return executeRoundUp(accessToken, savingsGoalName, savingsGoalAmount, accountIndex, response);
        });
    }

}