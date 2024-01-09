package com.leondailani.starlingroundup.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

import static com.leondailani.starlingroundup.utils.JsonUtil.parseJson;

public abstract class BaseApiClient {
    protected final HttpClient httpClient;
    protected final String accessToken;

    public BaseApiClient(String accessToken, HttpClient httpClient) {
        this.accessToken = accessToken;
        this.httpClient = httpClient != null ? httpClient : HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    }

    public BaseApiClient(String accessToken){
        this(accessToken,null);
    }

    protected <T> T sendRequest(HttpRequest request, Class<T> model) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200 || response.statusCode() == 201 ) {
            return parseJson(response.body(), model);

        } else{
            throw new IOException("Unexpected API Response: " + response.statusCode() + " - " + response.body());
        }
    }

    protected HttpRequest makeRequestURIGET(String url){
        return HttpRequest.newBuilder()
                .uri(URI.create(url)) // API Endpoint
                .header("Accept", "application/json") // Accept header for JSON
                .header("Authorization", "Bearer " + this.accessToken) // Authorization header with Bearer token
                .build();
    }

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
