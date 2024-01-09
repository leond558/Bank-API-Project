package com.leondailani.starlingroundup.api;

import com.leondailani.starlingroundup.models.ClientAccounts;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

/**
 * Handles the API call for the account information.
 */
public class AccountsApiClient  extends  BaseApiClient{

    private final String path = "https://api-sandbox.starlingbank.com/api/v2/accounts";

    public AccountsApiClient(String accessToken){
        super(accessToken);
    }

    public AccountsApiClient(String accessToken, HttpClient httpClient){ super(accessToken,httpClient);}

    /**
     * Returns the account information from a corresponding API call to Starling Bank.
     * Method getAccounts retrieves all the accounts and account information pertaining to a particular customer.
     * @return A ClientAccounts object that is initialised from the information contained in
     * the body of the response from the API Call
     */
    public ClientAccounts getAccounts() throws IOException, InterruptedException{

        HttpRequest request = makeRequestURIGET(path);

        return sendRequest(request,ClientAccounts.class);

    }
}
