package com.naman.capstone.exception;

/**
 * custom exception for user not found
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}