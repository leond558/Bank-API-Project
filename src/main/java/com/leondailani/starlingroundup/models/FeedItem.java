package com.leondailani.starlingroundup.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Represents a transaction entity in the Starling Bank API.
 * These transactions form a transaction feed.
 * Each FeedItem transaction contains information about the transaction.
 *
 * Only need to consider direction of the transaction (we only want outgoing ones
 * for the goal set up) and the amount (the currency and monetary value of the transaction).
 * This class is used to deserialize account information
 * from JSON responses returned by the API.
 */
public class FeedItem {
    @JsonProperty("direction")
    private String direction;

    @JsonProperty("amount")
    private Amount amount;

    public FeedItem(){

    }


    // Inner class to represent the amount structure
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amount {
        @JsonProperty("minorUnits")
        private int minorUnits;

        // getters and setters
        public int getMinorUnits() {
            return minorUnits;
        }

//        A set method is defined here for use in the unit test classes.
        public void setMinorUnits(int minorUnits) {
            this.minorUnits = minorUnits;
        }
    }

    // getters and setters for outer class
    public String getDirection() {
        return direction;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}