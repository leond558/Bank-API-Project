package com.leondailani.starlingroundup.api;

import com.leondailani.starlingroundup.models.FeedItems;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles the API call for the transaction feed information.
 */

public class TransactionsApiClient extends BaseApiClient {

    private final String path = "https://api-sandbox.starlingbank.com/api/v2/feed/account/%s/category/%s/transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s";

    public TransactionsApiClient(String accessToken){
        super(accessToken);
    }
    public TransactionsApiClient(String accessToken, HttpClient httpClient){ super(accessToken,httpClient);}

    /**
     * Method to make an API call to fetch the transaction feed over the last week.
     * Feed consists of items that include information pertaining to that particular transaction
     * @param accountUid The account ID to which the transaction feed desired belongs.
     * @param categoryUid The category ID to which the transaction feed desired belongs.
     * @param startOfWeek The start week time from which we consider transactions from.
     * @param endOfWeek The end week time after which we no longer consider transactions for the transaction feed generated.
     * @return Returns a FeedItems object that contains individual FeedItem objects that include indvidual transactions.
     */
    public FeedItems getWeeklyTransactions(String accountUid, String categoryUid, ZonedDateTime startOfWeek, ZonedDateTime endOfWeek) throws IOException, InterruptedException {

        String url = String.format(path,accountUid,categoryUid,
                startOfWeek.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                endOfWeek.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));


        HttpRequest request = makeRequestURIGET(url);

        return sendRequest(request,FeedItems.class);

    }
}
