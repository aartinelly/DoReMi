package com.example.geektrust.exceptions;

public class NoSubscriptionFoundException extends RuntimeException{
    public NoSubscriptionFoundException(String msg){
        super(msg);
    }
}
