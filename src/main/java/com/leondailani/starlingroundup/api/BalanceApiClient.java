package com.leondailani.starlingroundup.api;

import com.leondailani.starlingroundup.models.Balance;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

/**
 * Handles the API call for the account balance information.
 */
public class BalanceApiClient extends BaseApiClient{

    public BalanceApiClient(String accessToken){
        super(accessToken);
    }
    public BalanceApiClient(String accessToken, HttpClient httpClient){ super(accessToken,httpClient);}


    /**
     * Method to check the balance of the account considered.
     * Used to determine whether there are sufficient funds in order to transfer
     * the round-up amount into the savings goal.
     * @param accountUid The ID of the account for which the balance is to be checked.
     * @return Returns a balance object containing the amount contained in the account.
     */
    public Balance getBalance(String accountUid) throws IOException, InterruptedException{

        String url = "https://api-sandbox.starlingbank.com/api/v2/accounts/" + accountUid + "/balance";

        HttpRequest request = makeRequestURIGET(url);

        return sendRequest(request,Balance.class);

    }
}
