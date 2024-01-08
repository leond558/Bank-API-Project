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


public class StarlingClient {

    private final HttpClient httpClient;
    private final String accessToken;

//Constructor that accepts a HTTPClient for testing
    public StarlingClient(String accessToken, HttpClient httpClient){
//        Initialising a HTTP Client instance when a 'StarlingClient' object is constructed
        //        Access token pertaining to the Starling client.
        this.accessToken = accessToken;
        this.httpClient = httpClient != null ? httpClient : HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public StarlingClient(String accessToken){
        this(accessToken,null);
    }

    /**
     * Returns the account information from a corresponding API call to Starling Bank.
     * Method getAccounts retrieves all the accounts and account information pertaining to a particular customer.
     * @return the body of the response from the API Call
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

        if (response.statusCode() == 200) {
//             Successful response to the API call
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), ClientAccounts.class);
        }
        else {
//            If the API response is not 200 (OK), throw an 'IOException' with the details of what went wrong.
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

    public FeedItems getWeeklyTransactions(String accountUid, String categoryUid, ZonedDateTime startOfWeek, ZonedDateTime endOfWeek) {
        try {
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

//                If the PUT call is successful, then can return the SavingGoal object
                if (goal.getSuccess()){
                    return goal;
                }
                else{
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

    public SavingsGoalTransfer addMoneyToSavingsGoal(String accountUid, String savingsGoalUid, int amount) {
        try {
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

//                If the PUT call is successful, then can return the SavingGoal object
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
}
