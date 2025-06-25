package com.example.userauthenticationservice.exceptions;

public class IncorrectPassword extends RuntimeException {
    public IncorrectPassword(String message) {
        super(message);
    }
}
