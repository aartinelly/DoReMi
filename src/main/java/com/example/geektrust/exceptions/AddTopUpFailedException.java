package com.example.geektrust.exceptions;

public class AddTopUpFailedException extends RuntimeException{
    public AddTopUpFailedException(String msg){
        super(msg);
    }
}
