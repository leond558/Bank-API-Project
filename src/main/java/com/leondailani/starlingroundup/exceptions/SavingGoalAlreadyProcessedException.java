package com.leondailani.starlingroundup.exceptions;

/**
 * Defines an exception to handle instances where the savings goal has already been
 * processed.
 */
public class SavingGoalAlreadyProcessedException extends Exception {
    public SavingGoalAlreadyProcessedException(String message){super(message);}
}
