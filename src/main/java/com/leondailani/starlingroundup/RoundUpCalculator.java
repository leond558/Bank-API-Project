package com.leondailani.starlingroundup;
import java.util.List;

public class RoundUpCalculator {

    public static int calculateTotalRoundUp(List<FeedItem> feedItems) {
        int totalRoundUp = 0;

        for (FeedItem item : feedItems) {
            // Only consider outgoing transactions
            if ("OUT".equals(item.getDirection())) {
                int minorUnits = item.getAmount().getMinorUnits();
                int roundUp = 100 - (minorUnits % 100); // Calculate the round-up amount

                // If the transaction amount is already a whole number, skip it
                if (roundUp != 100) {
                    totalRoundUp += roundUp;
                }
            }
        }

        return totalRoundUp; // Returns the total round-up in minor units
    }
}
