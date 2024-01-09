package com.leondailani.starlingroundup.exceptions;

/**
 * Defines an exception to handle instances when the balance of the account is 0 and thus
 * no funds can be transferred into the savings goal.
 */
public class BalanceZeroException extends Exception{
    public  BalanceZeroException(String message){
        super(message);
    }
}
