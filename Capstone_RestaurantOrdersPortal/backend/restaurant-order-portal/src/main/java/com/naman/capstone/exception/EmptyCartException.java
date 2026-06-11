package com.naman.capstone.exception;

/**
 * Thrown when user tries to place order with empty cart
 */
public class EmptyCartException extends RuntimeException {

    public EmptyCartException(String message) {
        super(message);
    }
}