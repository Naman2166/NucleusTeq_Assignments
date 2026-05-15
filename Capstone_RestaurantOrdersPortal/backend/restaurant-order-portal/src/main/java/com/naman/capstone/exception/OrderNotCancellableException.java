package com.naman.capstone.exception;

/**
 * custom exception thrown when order cannot be cancelled
 */
public class OrderNotCancellableException extends RuntimeException {

    public OrderNotCancellableException(String message) {
        super(message);
    }
}
