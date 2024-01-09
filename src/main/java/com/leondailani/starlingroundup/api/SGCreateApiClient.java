package com.leondailani.starlingroundup.api;

import com.leondailani.starlingroundup.exceptions.SavingGoalFailException;
import com.leondailani.starlingroundup.models.SavingsGoal;
import com.leondailani.starlingroundup.models.SavingsGoalRequest;

import java.io.IOException;
import java.net.http.HttpRequest;

import static com.leondailani.starlingroundup.utils.JsonUtil.goalRequestToString;

/**
 * Handles the API call for the saving goal creation information.
 */
public class SGCreateApiClient extends BaseApiClient{

    public SGCreateApiClient(String accessToken){
        super(accessToken);
    }

    /**
     * Creates a saving goal for a particular account.
     * Takes a goalRequest object which contains information about how the goal should be set up including
     * the name for the goal, the monetary amount for the goal and the currency.
     * Checks that the creation of the goal has been successful and if not throws a corresponding error.
     * @param accountUid The account ID for which the goal is to be tied to.
     * @param goalRequest The object containing the necessary information about the goal in order to construct it
     * @return Returns a SavingGoal object which includes the ID of the goal created.
     */
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
