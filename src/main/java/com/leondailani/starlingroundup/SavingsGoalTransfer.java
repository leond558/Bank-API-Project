package com.leondailani.starlingroundup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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