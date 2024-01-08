package com.leondailani.starlingroundup.utils;
import com.leondailani.starlingroundup.models.FeedItem;

import java.util.List;

/**
 * A utility class to handle use calculation of the round-up amount
 */
public class RoundUpCalculator {

    /**
     * A method for calculation of the round-up amount from a list of transactions.
     * @param feedItems The list of transactions being considered for calculation of the
     *                  round-up amount.
     * @return An integer corresponding to the round-up amount.
     */
    public static int calculateTotalRoundUp(List<FeedItem> feedItems) {
        int totalRoundUp = 0;

        for (FeedItem item : feedItems) {
            // Only consider outgoing transactions
            if ("OUT".equals(item.getDirection())) {
                int minorUnits = item.getAmount().getMinorUnits();
//                 Calculate the round-up amount as the amount of pence required to get
//                the transaction to a whole number
                int roundUp = 100 - (minorUnits % 100);

//                 If the transaction amount is already a whole number, skip it
                if (roundUp != 100) {
                    totalRoundUp += roundUp;
                }
            }
        }

//         Returns the total round-up in pence (minor units)
        return totalRoundUp;
    }
}
