package com.naman.assignment.exception;

// This exception is thrown when user sends invalid input
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}