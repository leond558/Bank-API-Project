package com.leondailani.starlingroundup;

import static com.leondailani.starlingroundup.utils.RoundUpExecutor.executeRoundUp;
import static spark.Spark.*;

/**
 * Main method which sets up the embedded web server from which the calls to create
 * the savings goal can be made. Using Spark for the server.
 */
public class Main {
    public static void main(String[] args) {
        // Setting the port for the embedded server
        port(8080);

        post("/roundup", (request, response) -> {
            // Parse and validate input parameters
            String accessToken = request.queryParams("access_token");
            String savingsGoalName = request.queryParams("savings_goal_name");
            String savingsGoalAmountString = request.queryParams("savings_goal_amount");
            String accountIndexString = request.queryParams("account_index");
            int savingsGoalAmount;
            int accountIndex;

//            Checking that all the input parameters are there and in the correct format.

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
                accountIndex = Integer.parseInt(accountIndexString);
            } catch (NumberFormatException e) {
                response.status(400); // Bad request if amount is not a valid integer
                return "Account index number must be a valid integer.";
            }

            // Execute the round-up functionality in a separate method for better code organization
            return executeRoundUp(accessToken, savingsGoalName, savingsGoalAmount, accountIndex, response);
        });
    }

}