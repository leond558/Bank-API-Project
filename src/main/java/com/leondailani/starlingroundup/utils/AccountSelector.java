package com.leondailani.starlingroundup.utils;

import com.leondailani.starlingroundup.models.Account;
import com.leondailani.starlingroundup.models.ClientAccounts;

/**
 * A utility class to handle account selection
 */
public class AccountSelector {

    /**
     * Method to select the account from a list of client accounts.
     * @param accounts The list of client accounts.
     * @param desiredAccount The index of the account requested.
     * @return The account with the index or if index is out of bounds, the first account.
     */
    public static Account selectAccount(ClientAccounts accounts, int desiredAccount){
        try{
            System.out.println(desiredAccount);
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
