package com.leondailani.starlingroundup.api;

import com.leondailani.starlingroundup.exceptions.SavingGoalFailException;
import com.leondailani.starlingroundup.models.SavingsGoal;
import com.leondailani.starlingroundup.models.SavingsGoalRequest;

import java.io.IOException;
import java.net.http.HttpRequest;

import static com.leondailani.starlingroundup.utils.JsonUtil.goalRequestToString;

public class SGCreateApiClient extends BaseApiClient{

    public SGCreateApiClient(String accessToken){
        super(accessToken);
    }

    public SavingsGoal createSavingsGoal(String accountUid, SavingsGoalRequest goalRequest) throws IOException, InterruptedException, SavingGoalFailException {

        String url = "https://api-sandbox.starlingbank.com/api/v2/account/" + accountUid + "/savings-goals";

        String requestBody = goalRequestToString(goalRequest);

        HttpRequest request = makeRequestURIPUT(url, requestBody);
        SavingsGoal goal = sendRequest(request,SavingsGoal.class);

        //                If the creation of the saving goal is successful, then can return the SavingGoal object
        if (goal.getSuccess()){
            return goal;
        }
        else{
//                    If the creation is unsuccessful, then raise the necessary exception.
            throw new SavingGoalFailException("Put call to create new SavingGoal failed.");
        }
    }
}
