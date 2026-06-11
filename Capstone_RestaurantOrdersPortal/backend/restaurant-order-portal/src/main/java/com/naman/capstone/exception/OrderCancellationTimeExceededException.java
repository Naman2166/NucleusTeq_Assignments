package com.naman.capstone.exception;

/**
 * custom exception for order cancellation time exceeded
 */
public class OrderCancellationTimeExceededException extends RuntimeException {

    public OrderCancellationTimeExceededException(String message) {
        super(message);
    }
}
