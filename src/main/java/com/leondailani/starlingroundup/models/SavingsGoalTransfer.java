package com.leondailani.starlingroundup.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a savings goal transfer result entity in the Starling Bank API.
 * This entity is returned as a result of the transferring funds into a savings goal.
 * This class is used to deserialize account information
 * from JSON responses returned by the API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SavingsGoalTransfer {
    @JsonProperty("transferUid")
    private String transferUid;

    @JsonProperty("success")
    private Boolean success;

    // getters and setters

    public String getTransferUid() {
        return transferUid;
    }
    public Boolean getSuccess(){
        return success;
    }

}