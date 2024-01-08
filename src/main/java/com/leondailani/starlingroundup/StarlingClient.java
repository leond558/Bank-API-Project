package com.leondailani.starlingroundup;

import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This class handles the communication and API calls with the Starling Bank API.
 * It provides methods to retrieve account details, fetch a transaction feed over
 * some period of time, to create a savings goal and to transfer money over to that goal.
 */

public class StarlingClient {

    private final HttpClient httpClient;
    private final String accessToken;

//Constructor that accepts a HTTPClient for unit testing
    public StarlingClient(String accessToken, HttpClient httpClient){
        //        Access token pertaining to the Starling client.
        this.accessToken = accessToken;
        //        Initialising a HTTP Client instance when a 'StarlingClient' object is constructed
        this.httpClient = httpClient != null ? httpClient : HttpClient.newBuilder()
//                Using HTTP_2 for efficient performance
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

//    Overloaded constructor that is used when only an access token is prvodided.
    public StarlingClient(String accessToken){
        this(accessToken,null);
    }

    /**
     * Returns the account information from a corresponding API call to Starling Bank.
     * Method getAccounts retrieves all the accounts and account information pertaining to a particular customer.
     * @return A ClientAccounts object that is initialised from the information contained in
     * the body of the response from the API Call
     */
    public ClientAccounts getAccounts() {
//        Calling the accounts API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api-sandbox.starlingbank.com/api/v2/accounts")) // API Endpoint
                .header("Accept", "application/json") // Accept header for JSON
                .header("Authorization", "Bearer " + this.accessToken) // Authorization header with Bearer token
                .build();

//      Saving the response received by the API
        try{
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

//             Successful response to the API call
        if (response.statusCode() == 200) {

//            Parsing the JSON response from the API call
//            Using Jackson to convert the JSON response into the appropriate object.
            ObjectMapper mapper = new ObjectMapper();
//            Returning a ClientAccounts object, passing the parsed JSON from the API call to the constructor
            return mapper.readValue(response.body(), ClientAccounts.class);
        }
        else {
//            If the API response is not 200 (OK), throw an 'IOException' with the details of what went wrong
            throw new IOException("Unexpected API Response: " + response.statusCode() + " - " + response.body());
        }
//         Catch and handle network or API request errors, this catches any interruptions to the API call.
    }
        catch (IOException | InterruptedException e) {
//            Detail why an exception was thrown.
            e.printStackTrace();
            return null;
    }
    }

    /**
     * Method to make an API call to fetch the transaction feed over the last week.
     * Feed consists of items that include information pertaining to that particular transaction
     * @param accountUid The account ID to which the transaction feed desired belongs.
     * @param categoryUid The category ID to which the transaction feed desired belongs.
     * @param startOfWeek The start week time from which we consider transactions from.
     * @param endOfWeek The end week time after which we no longer consider transactions for the transaction feed generated.
     * @return Returns a FeedItems object that contains individual FeedItem objects that include indvidual transactions.
     */
    public FeedItems getWeeklyTransactions(String accountUid, String categoryUid, ZonedDateTime startOfWeek, ZonedDateTime endOfWeek) {
        try {
//            Constructing the string for the API call to include all the necessary information about the client and their account
            String url = String.format("https://api-sandbox.starlingbank.com/api/v2/feed/account/%s/category/%s/transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s",
                    accountUid,
                    categoryUid,
                    startOfWeek.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    endOfWeek.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), FeedItems.class);
            } else {
                throw new IOException("Unexpected API Response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a saving goal for a particular account.
     * Takes a goalRequest object which contains information about how the goal should be set up including
     * the name for the goal, the monetary amount for the goal and the currency.
     * Checks that the creation of the goal has been successful and if not throws a corresponding error.
     * @param accountUid The account ID for which the goal is to be tied to.
     * @param goalRequest The object containing the necessary information about the goal in order to construct it
     * @return Returns a SavingGoal object which includes the ID of the goal created.
     */
    public SavingsGoal createSavingsGoal(String accountUid, SavingsGoalRequest goalRequest) {
        try {
            String url = "https://api-sandbox.starlingbank.com/api/v2/account/" + accountUid + "/savings-goals";
            ObjectMapper mappergoal = new ObjectMapper();
            String requestBody = mappergoal.writeValueAsString(goalRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                ObjectMapper mapperresponse = new ObjectMapper();
//                Response of the API call will be JSON that follows the SavingsGoal class
//                Let's create an object with the body of the response in order to check whether the PUT call has been successful
                SavingsGoal goal = mapperresponse.readValue(response.body(), SavingsGoal.class);

//                If the creation of the saving goal is successful, then can return the SavingGoal object
                if (goal.getSuccess()){
                    return goal;
                }
                else{
//                    If the creation is unsuccessful, then raise the necessary exception.
                    throw new SavingGoalFailException("Put call to create new SavingGoal failed.");
                }

            } else {
                throw new IOException("Unexpected API Response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException | SavingGoalFailException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Transfers funds into a Savings Goal.
     * @param accountUid The account ID that the savings goal is attached to.
     * @param savingsGoalUid The ID for the saving goals for which funds are being transferred to.
     * @param amount The amount object that includes the currency and value in pence for the funds
     *               being transferred into the saving goal.
     * @return A SavingsGoalTransfer object that includes the transfer ID and the state of the transfer.
     */
    public SavingsGoalTransfer addMoneyToSavingsGoal(String accountUid, String savingsGoalUid, int amount) {
        try {
//            Creating a random transfer ID for use in the API call
            String url = "https://api-sandbox.starlingbank.com/api/v2/account/" + accountUid + "/savings-goals/" + savingsGoalUid + "/add-money/" + UUID.randomUUID();
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> amountMap = new HashMap<>();
            amountMap.put("amount", new HashMap<String, Object>() {{
                put("currency", "GBP");
                put("minorUnits", amount);
            }});

            String requestBody = mapper.writeValueAsString(amountMap);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 || response.statusCode() == 201) {
                ObjectMapper mapperresponse = new ObjectMapper();
//                Response of the API call will be JSON that follows the SavingsGoal class
//                Let's create an object with the body of the response in order to check whether the PUT call has been successful
                SavingsGoalTransfer goalTransfer = mapperresponse.readValue(response.body(), SavingsGoalTransfer.class);

//                If the transfer of funds is successful, then can return the SavingsGoalTransfer object.
                if (goalTransfer.getSuccess()) {
                    return goalTransfer;
                } else {
                    throw new SavingGoalFailException("Put call to transfer amount into SavingGoal failed.");
                }

            } else {
                throw new IOException("Unexpected API Response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException | SavingGoalFailException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Method to check the balance of the account considered.
     * Used to determine whether there are sufficient funds in order to transfer
     * the round-up amount into the savings goal.
     * @param accountUid The ID of the account for which the balance is to be checked.
     * @return Returns a balance object containing the amount contained in the account.
     */
    public Balance accountBalanceChecker(String accountUid) {
        try {
            String url = "https://api-sandbox.starlingbank.com/api/v2/accounts/" + accountUid + "/balance";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), Balance.class);
            } else {
                throw new IOException("Unexpected API Response: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
