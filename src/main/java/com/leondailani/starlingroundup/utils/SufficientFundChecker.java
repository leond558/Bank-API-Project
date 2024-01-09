package com.leondailani.starlingroundup.utils;

import com.leondailani.starlingroundup.api.BalanceApiClient;
import com.leondailani.starlingroundup.exceptions.BalanceZeroException;
import com.leondailani.starlingroundup.models.Balance;

import java.io.IOException;

public class SufficientFundChecker {

    private BalanceApiClient balanceClient;
    private Balance accountBalance;

    public int calculateFunds(int roundUpAmount) throws BalanceZeroException {

//        Retrieve the account balance

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

    public SufficientFundChecker(String accessToken, String accountUid) throws IOException, InterruptedException {
        this.balanceClient = new BalanceApiClient(accessToken);
        this.accountBalance = this.balanceClient.getBalance(accountUid);
    }

//    Overloaded constructor for testing the logic
    public SufficientFundChecker(Balance balance) {
        this.balanceClient = null;
        this.accountBalance = balance;
    }
}
