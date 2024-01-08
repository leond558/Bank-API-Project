package com.leondailani.starlingroundup;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.DayOfWeek;

/**
 * The main method takes all the transactions over the past week for a Starling Bank customer's
 * first account. It then calculates the round-up. It then creates a savings goal with a name and
 * target amount that can be adjusted accordingly. It then transfers funds from the round-up into
 * the savings goal accounting for whether these funds exist in the account or not (whether there
 * is enough money in the account to make the transfer).
 */
public class Main {
    public static void main(String[] args) {
        try {
            //Name for the savings goal
            String savingsGoalName = "PastWeekSaving";
            // Amount for the savings goal
            int savingAmount = 100000;

            // Load the access token and initialize the StarlingClient
            String accessToken = AccessTokenLoader.loadAccessToken();
            StarlingClient client = new StarlingClient(accessToken);

            // Retrieve account details
            ClientAccounts accounts = client.getAccounts();
            if (accounts == null || accounts.getAccounts().isEmpty()) {
                System.err.println("No accounts available or token incorrectly input in config file.");
                return;
            }

            // Get the first account's details
            Account acc = accounts.getAccounts().get(0);
            String accountUid = acc.getAccountUid();
            String categoryUid = acc.getDefaultCategory();

            // Determine the start and end of the current week
            ZonedDateTime startOfWeek = ZonedDateTime.now().with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
            ZonedDateTime endOfWeek = startOfWeek.plusDays(7);

            // Fetch transactions for the specified week
            FeedItems transactionDetails = client.getWeeklyTransactions(accountUid, categoryUid, startOfWeek, endOfWeek);
            if (transactionDetails == null || transactionDetails.getFeedItems().isEmpty()) {
                System.err.println("No transaction data available for the specified week.");
                return;
            }

            // Calculate the round-up amount from the week's transactions
            int roundUpAmount = RoundUpCalculator.calculateTotalRoundUp(transactionDetails.getFeedItems());
            // If the roundup is zero, the API call will fail so we can't generate a zero savings gaol
            if (roundUpAmount <= 0) {
                System.out.println("No round-up amount calculated for the current week. Thus no savings goal can be created");
                return;
            }

            // Create a savings goal
            SavingsGoalRequest goalRequest = new SavingsGoalRequest(savingsGoalName, "GBP", savingAmount);
            SavingsGoal savingsGoalResponse = client.createSavingsGoal(accountUid, goalRequest);
            if (savingsGoalResponse == null) {
                System.err.println("Failed to receive a valid response for savings goal creation.");
                return;
            }

            // Transfer the round-up amount to the savings goal
            // Check that the account has sufficient money to first transfer funds.
            Balance balance = client.accountBalanceChecker(accountUid);
            if (balance.getAmount().getMinorUnits()< roundUpAmount){
                System.err.println("Account has insufficient funds to transfer round up amount to the savings goal");
                return;
            }

            //Transfer the funds.
            String savinggoalUId = savingsGoalResponse.getSavingsGoalUid();
            SavingsGoalTransfer transferResponse = client.addMoneyToSavingsGoal(accountUid, savinggoalUId, roundUpAmount);
            if (transferResponse == null || !transferResponse.getSuccess()) {
                System.err.println("Failed to transfer funds to the savings goal.");
                return;
            }

            // Output confirmation message
            String outputConfirm = String.format("Savings Goal with UID (%s) of %s pence successfully created for account with UID (%s), with %s pence deposited into the goal from the past week to date's transactions.",
                    savingsGoalResponse.getSavingsGoalUid(),
                    goalRequest.getTarget().getMinorUnits(),
                    acc.getAccountUid(),
                    roundUpAmount);

            System.out.println(outputConfirm);

        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
