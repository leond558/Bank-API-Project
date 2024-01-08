package com.leondailani.starlingroundup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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