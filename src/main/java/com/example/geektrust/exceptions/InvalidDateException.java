package com.example.geektrust.exceptions;

public class InvalidDateException extends RuntimeException{
    public InvalidDateException(String msg){
        super(msg);
    }
}
