package com.leondailani.starlingroundup;

import com.leondailani.starlingroundup.api.*;
import com.leondailani.starlingroundup.exceptions.SavingGoalFailException;
import com.leondailani.starlingroundup.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the StarlingClient class.
 * These tests mock external HTTP requests to the Starling Bank API.
 */
class ApiClientTests {

    private AccountsApiClient accountsApiClient;
    private TransactionsApiClient transactionsApiClient;
    private SGCreateApiClient sgCreateApiClient;
    private SGTransferApiClient sgTransferApiClient;
    private BalanceApiClient balanceApiClient;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    /**
     * Setup method to initialize mocks and create a StarlingClient instance with mocked HttpClient.
     * This method runs before each test.
     */
    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);
        accountsApiClient = new AccountsApiClient("fakeAccessToken", mockHttpClient);
        transactionsApiClient = new TransactionsApiClient("fakeAccessToken", mockHttpClient);
        sgCreateApiClient = new SGCreateApiClient("fakeAccessToken", mockHttpClient);
        sgTransferApiClient = new SGTransferApiClient("fakeAccessToken", mockHttpClient);
        balanceApiClient = new BalanceApiClient("fakeAccessToken", mockHttpClient);
    }

    /**
     * Test for getAccounts method of StarlingClient.
     * Validates that the response is correctly parsed and the expected values are returned.
     */
    @Test
    void testGetAccounts() throws IOException, InterruptedException {
        // Prepare mock response
        when(mockResponse.body()).thenReturn("{\"accounts\":[{\"accountUid\":\"example\"}]}");
        when(mockResponse.statusCode()).thenReturn(200);

        // Execute the method to test
        ClientAccounts result = accountsApiClient.getAccounts();

        // Verify the results
        assertNotNull(result);
        assertFalse(result.getAccounts().isEmpty());
        assertEquals("example", result.getAccounts().getFirst().getAccountUid());
    }

    /**
     * Test for getWeeklyTransactions
     * Checks if the method correctly handles and parses the response.
     */
    @Test
    void testGetWeeklyTransactions() throws IOException, InterruptedException {
        // Prepare the mock response
        when(mockResponse.body()).thenReturn("{\"feedItems\":[{\"amount\":{\"currency\":\"GBP\",\"minorUnits\":841}}]}");
        when(mockResponse.statusCode()).thenReturn(200);

        // Mock the HttpClient to return the mock response
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        // Prepare parameters for the method call
        String accountUid = "example-uid";
        String categoryUid = "example-category";
        ZonedDateTime startOfWeek = ZonedDateTime.now().with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime endOfWeek = startOfWeek.plusDays(7);

        // Execute the method to test
        FeedItems result = transactionsApiClient.getWeeklyTransactions(accountUid, categoryUid, startOfWeek, endOfWeek);

        // Verify the results
        assertNotNull(result);
        assertFalse(result.getFeedItems().isEmpty());
        assertEquals(841, result.getFeedItems().getFirst().getAmount().getMinorUnits());
    }

    /**
     * Test for createSavingsGoal method.
     * Ensures that the method correctly interprets a successful response.
     */
    @Test
    void testCreateSavingsGoal() throws IOException, InterruptedException, SavingGoalFailException {
        // Prepare the mock response
        when(mockResponse.body()).thenReturn("{\"success\":true,\"errors\":[],\"savingsGoalUid\":\"example-savings-goal-uid\"}");
        when(mockResponse.statusCode()).thenReturn(201);

        // Mock the HttpClient to return the mock response
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        // Prepare parameters for the method call
        String accountUid = "example-uid";
        SavingsGoalRequest goalRequest = new SavingsGoalRequest("Trip to Paris", "GBP", 123456);

        // Execute the method to test
        SavingsGoal result = sgCreateApiClient.createSavingsGoal(accountUid, goalRequest);

        // Verify the results
        assertNotNull(result);
        assertTrue(result.getSuccess());
        assertEquals("example-savings-goal-uid", result.getSavingsGoalUid());
    }

    /**
     * Test for addMoneyToSavingsGoal method.
     * Validates that the funds transfer is processed correctly.
     */
    @Test
    void testAddMoneyToSavingsGoal() throws IOException, InterruptedException, SavingGoalFailException {
        // Prepare the mock response
        when(mockResponse.body()).thenReturn("{\"success\":true,\"errors\":[]}");
        when(mockResponse.statusCode()).thenReturn(200);

        // Mock the HttpClient to return the mock response

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        // Prepare parameters for the method call
        String accountUid = "example-uid";
        String savingsGoalUid = "example-savings-goal-uid";
        int amount = 1000; // Example amount in minor units

        // Execute the method to test
        SavingsGoalTransfer result = sgTransferApiClient.addMoneyToSavingsGoal(accountUid, savingsGoalUid, amount);

        // Verify the results
        assertNotNull(result);
        assertTrue(result.getSuccess());
    }

    /**
     * Test for addMoneyToSavingsGoal method.
     * Validates that the funds transfer is processed correctly.
     */
    @Test
    void testgetBalance() throws IOException, InterruptedException {
        // Prepare the mock response
        when(mockResponse.body()).thenReturn("{\"amount\":{\"minorUnits\":1000}}");
        when(mockResponse.statusCode()).thenReturn(200);

        // Mock the HttpClient to return the mock response

        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);

        // Prepare parameters for the method call
        String accountUid = "example-uid";

        // Execute the method to test
        Balance result = balanceApiClient.getBalance(accountUid);

        // verify the results
        assertNotNull(result);
        assertEquals(1000, result.getAmount().getMinorUnits());

    }




}

