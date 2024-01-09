package com.leondailani.starlingroundup.utils;

import com.leondailani.starlingroundup.api.AccountsApiClient;
import com.leondailani.starlingroundup.api.SGCreateApiClient;
import com.leondailani.starlingroundup.api.SGTransferApiClient;
import com.leondailani.starlingroundup.api.TransactionsApiClient;
import com.leondailani.starlingroundup.exceptions.NoRoundUpException;
import com.leondailani.starlingroundup.exceptions.SavingGoalAlreadyProcessedException;
import com.leondailani.starlingroundup.models.*;
import spark.Response;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static com.leondailani.starlingroundup.utils.AccountSelector.selectAccount;
import static com.leondailani.starlingroundup.utils.Hasher.*;

/**
 * This class runs the main round up functionality.
 * The executeRoundUp method takes all the transactions over the past week for a Starling Bank customer's
 * account. It then calculates the round-up. It then creates a savings goal with a name and
 * target amount that can be adjusted accordingly. It then transfers funds from the round-up into
 * the savings goal accounting for whether these funds exist in the account or not (whether there
 * is enough money in the account to make the transfer).
 * A confirmation is thus returned to indicate the success of the saving goal creation.
 * The creation of this savings goal is then documented with a hash to prevent the same savings goal being created.
 */
public class RoundUpExecutor {

    // The file where hashes are stored
    private static final String hashFilePath = "src/main/resources/hashes.txt";

    /**
     * The method for calculating the round up and creating the corresponding savings goal.
     * @param accessToken Access token pertaining to the Starling Bank customer
     * @param savingsGoalName Name of the savings goal to be created.
     * @param savingsGoalAmount The amount for the savings goal.
     * @param accountIndex Which account for the customer should be used.
     * @param response
     * @return A confirmation message String, confirming the successful creation of the Savings Goal.
     */
    public static String executeRoundUp(String accessToken, String savingsGoalName, int savingsGoalAmount, int accountIndex, Response response) {
        try {
            AccountsApiClient accountsClient = new AccountsApiClient(accessToken);

            // Retrieve account details
            ClientAccounts accounts = accountsClient.getAccounts();
            if (accounts == null || accounts.getAccounts().isEmpty()) {
                throw new IllegalArgumentException("No accounts available.");
            }

            // Get the first account's details
            Account account = selectAccount(accounts,accountIndex);
            String accountUid = account.getAccountUid();
            String categoryUid = account.getDefaultCategory();

// Set the start of the week to today, truncated to the start of the day.
            ZonedDateTime startOfWeek = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);
            ZonedDateTime endOfWeek = startOfWeek.plusDays(7);

            // Fetch transactions for the specified week
            TransactionsApiClient transactionsClient = new TransactionsApiClient(accessToken);
            FeedItems transactionDetails = transactionsClient.getWeeklyTransactions(accountUid, categoryUid, startOfWeek, endOfWeek);
            if (transactionDetails == null || transactionDetails.getFeedItems().isEmpty()) {
                throw new IllegalArgumentException("No transaction data available for the specified week.");
            }

            String hash = generateHash(accountUid, startOfWeek, endOfWeek);

            // Check if this hash already exists
            if (checkIfHashExists(hash, hashFilePath)) {
                throw new SavingGoalAlreadyProcessedException("A savings goal for this timeframe already exists.");
            }

            // Calculate the round-up amount from the week's transactions
            int roundUpAmount = RoundUpCalculator.calculateTotalRoundUp(transactionDetails.getFeedItems());
            if (roundUpAmount <= 0) {
                throw new NoRoundUpException("No round-up amount calculated for the current week.");
            }

            // Create a savings goal
            // The response will never be null because the success check at the API call level will fail
            SGCreateApiClient creationClient = new SGCreateApiClient(accessToken);
            SavingsGoalRequest goalRequest = new SavingsGoalRequest(savingsGoalName,"GBP",savingsGoalAmount);
            SavingsGoal savingsGoalResponse = creationClient.createSavingsGoal(accountUid, goalRequest);

//            Check whether the account has sufficient funds to transfer into the goal and check how much it can transfer in
            SufficientFundChecker fundCheck = new SufficientFundChecker(accessToken, accountUid);
            int transferAmount = fundCheck.calculateFunds(roundUpAmount);


            // Transfer the round-up amount to the savings goal
            String savingsGoalUid = savingsGoalResponse.getSavingsGoalUid();
            SGTransferApiClient transferClient = new SGTransferApiClient(accessToken);
            SavingsGoalTransfer transferResponse = transferClient.addMoneyToSavingsGoal(accountUid, savingsGoalUid, roundUpAmount);


            storeHash(hash,hashFilePath);

            // Confirmation message

            return String.format("'%s' Savings Goal Successfully Created \n " +
                            "with UID (%s). \n" +
                            "Target Amount = %s \n" +
                            "Created for account with UID (%s). \n" +
                            " %s pence deposited into the goal from the past week's transactions. \n" +
                            "TransferUid for the deposit (%s)",
                    savingsGoalName, savingsGoalUid, savingsGoalAmount, accountUid, transferAmount, transferResponse.getTransferUid());

        } catch (IllegalArgumentException e) {
            response.status(400); // Bad request for illegal argument
            return e.getMessage();
        } catch (Exception e) {
            response.status(500); // Internal server error for unhandled exceptions
            return "An error occurred: " + e.getMessage();
        }
    }
}
