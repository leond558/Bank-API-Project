package com.leondailani.starlingroundup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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