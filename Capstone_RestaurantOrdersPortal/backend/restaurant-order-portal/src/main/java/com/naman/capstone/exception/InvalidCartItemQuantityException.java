package com.naman.capstone.exception;

/**
 * custom exception for invalidate cart item quantity
 */
public class InvalidCartItemQuantityException extends RuntimeException {

    public InvalidCartItemQuantityException(String message) {
        super(message);
    }
}