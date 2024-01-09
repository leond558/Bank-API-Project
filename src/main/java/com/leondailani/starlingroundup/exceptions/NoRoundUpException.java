package com.leondailani.starlingroundup.exceptions;

/**
 * Defines an exception to handle instances when there is no round up and thus
 * no savings goal can be created.
 */
public class NoRoundUpException extends Exception{
    public  NoRoundUpException(String message){
        super(message);
    }
}
