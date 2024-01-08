package com.leondailani.starlingroundup.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a savings goal entity in the Starling Bank API.
 * This entity is returned as a result of the Put call to create a savings goal.
 * This class is used to deserialize account information
 * from JSON responses returned by the API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SavingsGoal {
    @JsonProperty("savingsGoalUid")
    private String savingsGoalUid;

    @JsonProperty("success")
    private Boolean success;

    // getters and setters
    public String getSavingsGoalUid(){
        return savingsGoalUid;
    }

    public Boolean getSuccess(){
        return success;
    }

}