package com.naman.assignment.exception;

// custom exception for handling invalid input

public class BadRequestException extends RuntimeException {

    // constructor to pass custom error message
    public BadRequestException(String message) {
        super(message);
    }
}