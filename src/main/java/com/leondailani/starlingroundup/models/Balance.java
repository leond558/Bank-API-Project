package com.leondailani.starlingroundup.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a balance of an account in the Starling Bank API.
 * This class is used to deserialize account information
 * from JSON responses returned by the API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {
    @JsonProperty("amount")
    private Amount amount;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Amount {
        @JsonProperty("minorUnits")
        private int minorUnits;

        // getters and setters
        public int getMinorUnits() {
            return minorUnits;
        }

    }

    public Amount getAmount() {
        return amount;
    }
}
