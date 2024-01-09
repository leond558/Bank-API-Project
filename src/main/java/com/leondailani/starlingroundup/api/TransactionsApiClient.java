package com.leondailani.starlingroundup.api;

import com.leondailani.starlingroundup.models.FeedItems;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionsApiClient extends BaseApiClient {

    private final String path = "https://api-sandbox.starlingbank.com/api/v2/feed/account/%s/category/%s/transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s";

    public TransactionsApiClient(String accessToken){
        super(accessToken);
    }

    public FeedItems getWeeklyTransactions(String accountUid, String categoryUid, ZonedDateTime startOfWeek, ZonedDateTime endOfWeek) throws IOException, InterruptedException {

        String url = String.format(path,accountUid,categoryUid,
                startOfWeek.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                endOfWeek.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));


        HttpRequest request = makeRequestURIGET(url);

        return sendRequest(request,FeedItems.class);

    }
}
