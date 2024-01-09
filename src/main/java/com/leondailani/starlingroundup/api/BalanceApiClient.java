package com.leondailani.starlingroundup.api;

import com.leondailani.starlingroundup.models.Balance;

import java.io.IOException;
import java.net.http.HttpRequest;

public class BalanceApiClient extends BaseApiClient{

    public BalanceApiClient(String accessToken){
        super(accessToken);
    }

    public Balance getBalance(String accountUid) throws IOException, InterruptedException{

        String url = "https://api-sandbox.starlingbank.com/api/v2/accounts/" + accountUid + "/balance";

        HttpRequest request = makeRequestURIGET(url);

        return sendRequest(request,Balance.class);

    }
}
