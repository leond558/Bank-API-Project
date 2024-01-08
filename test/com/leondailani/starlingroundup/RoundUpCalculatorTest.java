package com.leondailani.starlingroundup;

import com.leondailani.starlingroundup.models.FeedItem;
import com.leondailani.starlingroundup.utils.RoundUpCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

/**
 * Test for the round-up calculation function.
 */
class RoundUpCalculatorTest {

    @Test
    void testCalculateTotalRoundUp() {
        // Create mock transactions
        FeedItem testItem1 = new FeedItem();
        FeedItem.Amount testItem1Amount = new FeedItem.Amount();
        testItem1.setDirection("OUT");
        testItem1Amount.setMinorUnits(123);
        testItem1.setAmount(testItem1Amount);

        FeedItem testItem2 = new FeedItem();
        FeedItem.Amount testItem2Amount = new FeedItem.Amount();
        testItem2.setDirection("OUT");
        testItem2Amount.setMinorUnits(123);
        testItem2.setAmount(testItem2Amount);

        //Create a mock transaction feed
        List<FeedItem> mockTransactions = Arrays.asList(
                testItem1,
                testItem2
                // Add more transactions as needed
        );

        // Call the method to test
        int totalRoundUp = RoundUpCalculator.calculateTotalRoundUp(mockTransactions);

        // Assert the expected result
        Assertions.assertEquals(154, totalRoundUp);

    }

    @Test
    /**
     * This test measures the edge case whereby there is no round-up.
     */
    void testCalculateTotalRoundUpZero() {
        // Create mock transactions
        FeedItem testItem1 = new FeedItem();
        FeedItem.Amount testItem1Amount = new FeedItem.Amount();
        testItem1.setDirection("OUT");
        testItem1Amount.setMinorUnits(100);
        testItem1.setAmount(testItem1Amount);

        FeedItem testItem2 = new FeedItem();
        FeedItem.Amount testItem2Amount = new FeedItem.Amount();
        testItem2.setDirection("OUT");
        testItem2Amount.setMinorUnits(200);
        testItem2.setAmount(testItem2Amount);

        List<FeedItem> mockTransactions = Arrays.asList(
                testItem1,
                testItem2
                // Add more transactions as needed
        );

        // Call the method to test
        int totalRoundUp = RoundUpCalculator.calculateTotalRoundUp(mockTransactions);

        // Assert the expected result
        Assertions.assertEquals(0, totalRoundUp);

    }
}
