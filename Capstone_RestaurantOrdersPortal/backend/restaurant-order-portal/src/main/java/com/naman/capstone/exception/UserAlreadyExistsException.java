package com.naman.capstone.exception;

/**
 * custom exception for user already exists
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}