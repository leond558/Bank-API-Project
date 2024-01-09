package com.leondailani.starlingroundup.api;

import com.leondailani.starlingroundup.models.ClientAccounts;

import java.io.IOException;
import java.net.http.HttpRequest;

public class AccountsApiClient  extends  BaseApiClient{

    private final String path = "https://api-sandbox.starlingbank.com/api/v2/accounts";

    public AccountsApiClient(String accessToken){
        super(accessToken);
    }

    public ClientAccounts getAccounts() throws IOException, InterruptedException{

        HttpRequest request = makeRequestURIGET(path);

        return sendRequest(request,ClientAccounts.class);

    }
}
