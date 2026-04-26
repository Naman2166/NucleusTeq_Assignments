package com.naman.capstone.exception;

/**
 * Thrown when a user tries to access or modify a resource without permission.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}