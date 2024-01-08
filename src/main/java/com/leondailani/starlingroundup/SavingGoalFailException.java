package com.leondailani.starlingroundup;

/**
 * Defines an exception to handle instances when creation or transfer of funds into
 * a savings goal fails.
 */
public class SavingGoalFailException extends Exception{

    public  SavingGoalFailException(String message){
        super(message);
    }
}
