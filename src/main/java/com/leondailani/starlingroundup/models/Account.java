package com.leondailani.starlingroundup.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an account entity in the Starling Bank API.
 * This class is used to deserialize account information
 * from JSON responses returned by the API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
    @JsonProperty("accountUid")
    private String accountUid;

    @JsonProperty("defaultCategory")
    private String defaultCategory;

    // Add getters and setters for accountUid and defaultCategory
    public String getAccountUid() {
        return accountUid;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

}