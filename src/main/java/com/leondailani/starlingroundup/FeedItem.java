package com.leondailani.starlingroundup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
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