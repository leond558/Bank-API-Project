package com.leondailani.starlingroundup.utils;

import com.leondailani.starlingroundup.models.Account;
import com.leondailani.starlingroundup.models.ClientAccounts;

public class AccountSelector {

    public static Account selectAccount(ClientAccounts accounts, int desiredAccount){
        try{
//            if the account desired exists, then retrieve that one.
            return accounts.getAccounts().get(desiredAccount);
        }
//        If the index for the account desired is out of bounds i.e. the account
//        specified does not exist, then just use the first account available.
        catch (IndexOutOfBoundsException e){
            System.out.println("Account index specified does not exist, using the first account available instead...." );
            return accounts.getAccounts().getFirst();

        }

    }
}
