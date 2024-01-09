package com.leondailani.starlingroundup.utils;

import com.leondailani.starlingroundup.api.BalanceApiClient;
import com.leondailani.starlingroundup.exceptions.BalanceZeroException;
import com.leondailani.starlingroundup.models.Balance;

import java.io.IOException;

public class SufficientFundChecker {

    public static int calculateFunds(String accessToken, String accountUid, int roundUpAmount) throws IOException, InterruptedException, BalanceZeroException {

//        Retrieve the account balance
        BalanceApiClient balanceClient = new BalanceApiClient(accessToken);
        Balance accountBalance = balanceClient.getBalance(accountUid);
        int balanceAmount = accountBalance.getAmount().getMinorUnits();

//        Check the account balance against the roundUp amount
//        If the balance is empty, throw an exception
        if (balanceAmount == 0) {
            throw new BalanceZeroException("The account has no money in it to contribute to the saving goal.");
        }
//Have some funds but this is less than the round-up, then just use remaining funds
        else if (balanceAmount < roundUpAmount) {
            return balanceAmount;
        }
        //If there are sufficient funds for the saving goal to be created with the round up
//then use that
        return roundUpAmount;
    }
}
