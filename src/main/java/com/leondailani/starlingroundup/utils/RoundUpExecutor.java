package com.leondailani.starlingroundup.utils;

import com.leondailani.starlingroundup.api.*;
import com.leondailani.starlingroundup.exceptions.NoRoundUpException;
import com.leondailani.starlingroundup.exceptions.SavingGoalAlreadyProcessedException;
import com.leondailani.starlingroundup.models.*;
import spark.Response;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static com.leondailani.starlingroundup.utils.AccountSelector.selectAccount;
import static com.leondailani.starlingroundup.utils.Hasher.*;
import static com.leondailani.starlingroundup.utils.SufficientFundChecker.calculateFunds;

public class RoundUpExecutor {

    // The file where hashes are stored
    private static final String hashFilePath = "src/main/resources/hashes.txt";

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

            // Determine the start and end of the current week
            ZonedDateTime startOfWeek = ZonedDateTime.now().with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
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
            int transferAmount = calculateFunds(accessToken,accountUid,roundUpAmount);


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
