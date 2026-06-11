package com.naman.capstone.exception;

/**
 * custom exception thrown when user add item from different restaurant in cart
 */
public class MultipleRestaurantCartException extends RuntimeException {
    public MultipleRestaurantCartException(String message) { super(message); }
}
