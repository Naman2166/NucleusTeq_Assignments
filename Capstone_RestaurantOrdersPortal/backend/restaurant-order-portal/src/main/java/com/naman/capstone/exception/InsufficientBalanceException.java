package com.naman.capstone.exception;

/**
 * Thrown when wallet balance is insufficient
 */
public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}