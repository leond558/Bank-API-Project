package com.leondailani.starlingroundup;

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

class StarlingClientTest {

    private StarlingClient starlingClient;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;


    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        when(mockHttpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
                .thenReturn(mockResponse);
        starlingClient = new StarlingClient("fakeAccessToken", mockHttpClient);
    }

    @Test
    void testGetAccounts() throws IOException, InterruptedException {
        // Prepare mock response
        when(mockResponse.body()).thenReturn("{\"accounts\":[{\"accountUid\":\"example\"}]}");
        when(mockResponse.statusCode()).thenReturn(200);

        // Execute the method to test
        ClientAccounts result = starlingClient.getAccounts();

        // Verify the results
        assertNotNull(result);
        assertFalse(result.getAccounts().isEmpty());
        assertEquals("example", result.getAccounts().get(0).getAccountUid());
    }

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
        FeedItems result = starlingClient.getWeeklyTransactions(accountUid, categoryUid, startOfWeek, endOfWeek);

        // Verify the results
        assertNotNull(result);
        assertFalse(result.getFeedItems().isEmpty());
        assertEquals(841, result.getFeedItems().get(0).getAmount().getMinorUnits());
    }

    @Test
    void testCreateSavingsGoal() throws IOException, InterruptedException {
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
        SavingsGoal result = starlingClient.createSavingsGoal(accountUid, goalRequest);

        // Verify the results
        assertNotNull(result);
        assertTrue(result.getSuccess());
        assertEquals("example-savings-goal-uid", result.getSavingsGoalUid());
    }

    @Test
    void testAddMoneyToSavingsGoal() throws IOException, InterruptedException {
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
        SavingsGoalTransfer result = starlingClient.addMoneyToSavingsGoal(accountUid, savingsGoalUid, amount);

        // Verify the results
        assertNotNull(result);
        assertTrue(result.getSuccess());
    }




}

