package com.leondailani.starlingroundup.api;

import com.leondailani.starlingroundup.exceptions.SavingGoalFailException;
import com.leondailani.starlingroundup.models.SavingsGoalTransfer;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.leondailani.starlingroundup.utils.JsonUtil.amountRequestToString;

public class SGTransferApiClient extends BaseApiClient{

    /**
     * Handles the API call for the saving goal transfer information.
     */

    public SGTransferApiClient(String accessToken){
        super(accessToken);
    }

    /**
     * Transfers funds into a Savings Goal.
     * @param accountUid The account ID that the savings goal is attached to.
     * @param savingsGoalUid The ID for the saving goals for which funds are being transferred to.
     * @param amount The amount object that includes the currency and value in pence for the funds
     *               being transferred into the saving goal.
     * @return A SavingsGoalTransfer object that includes the transfer ID and the state of the transfer.
     */
    public SavingsGoalTransfer addMoneyToSavingsGoal(String accountUid, String savingsGoalUid, int amount) throws IOException, InterruptedException, SavingGoalFailException {

        String url = "https://api-sandbox.starlingbank.com/api/v2/account/" + accountUid + "/savings-goals/" + savingsGoalUid + "/add-money/" + UUID.randomUUID();
        Map<String, Object> amountMap = new HashMap<>();
        amountMap.put("amount", new HashMap<String, Object>() {{
            put("currency", "GBP");
            put("minorUnits", amount);
        }});

        String requestBody = amountRequestToString(amountMap);

        HttpRequest request = makeRequestURIPUT(url, requestBody);
        SavingsGoalTransfer goalTransfer = sendRequest(request,SavingsGoalTransfer.class);

        //                If the creation of the saving goal is successful, then can return the SavingGoal object
        if (goalTransfer.getSuccess()){
            return goalTransfer;
        }
        else{
//                    If the creation is unsuccessful, then raise the necessary exception.
            throw new SavingGoalFailException("Put call to create new SavingGoal failed.");
        }
    }
}
