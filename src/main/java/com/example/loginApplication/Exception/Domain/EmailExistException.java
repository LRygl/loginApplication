package com.example.loginApplication.Exception.Domain;

public class EmailExistException extends Exception{
    public EmailExistException(String message) {
        super(message);
    }
}
