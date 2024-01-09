package com.leondailani.starlingroundup.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

import static com.leondailani.starlingroundup.utils.JsonUtil.parseJson;

/**
 * This  abstract class handles the communication and API calls with the Starling Bank API.
 * It provides methods for making the URIs for both Get and put requests.
 * It contains a method that allows for the sending of a request.
 */
public abstract class BaseApiClient {
    protected final HttpClient httpClient;
    protected final String accessToken;

    //Constructor that accepts a HTTPClient for unit testing
    public BaseApiClient(String accessToken, HttpClient httpClient) {
        //        Access token pertaining to the Starling client.
        this.accessToken = accessToken;
        //        Initialising a HTTP Client instance when an ApiClient object is constructed
        //                Using HTTP_2 for efficient performance
        this.httpClient = httpClient != null ? httpClient : HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    }

    //    Overloaded constructor that is used when only an access token is prvodided.
    public BaseApiClient(String accessToken){
        this(accessToken,null);
    }

    /**
     * This method takes the request and parses the JSON output, returning an object of
     * the return class of the associated API call.
     * @param request The HTTP request.
     * @param model The return class of the associated API call.
     * @return An object of the return class
     * @param <T>
     * @throws IOException
     * @throws InterruptedException
     */
    protected <T> T sendRequest(HttpRequest request, Class<T> model) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //             Successful response to the API call
        if (response.statusCode() == 200 || response.statusCode() == 201 ) {
            //            Parsing the JSON response from the API call
//            Using Jackson to convert the JSON response into the appropriate object.
            return parseJson(response.body(), model);

        } else{
            //            If the API response is not 200 (OK), throw an 'IOException' with the details of what went wrong
            throw new IOException("Unexpected API Response: " + response.statusCode() + " - " + response.body());
        }
    }

    /**
     * Generates a URI for a GET request.
     * @param url
     * @return The HTTP Request for a Get request.
     */
    protected HttpRequest makeRequestURIGET(String url){
        return HttpRequest.newBuilder()
                .uri(URI.create(url)) // API Endpoint
                .header("Accept", "application/json") // Accept header for JSON
                .header("Authorization", "Bearer " + this.accessToken) // Authorization header with Bearer token
                .build();
    }

    /**
     * Generates a URI for a PUT request.
     * @param url
     * @return The HTTP Request for a PUT request.
     */
    protected HttpRequest makeRequestURIPUT(String url, String requestBody){
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

}
