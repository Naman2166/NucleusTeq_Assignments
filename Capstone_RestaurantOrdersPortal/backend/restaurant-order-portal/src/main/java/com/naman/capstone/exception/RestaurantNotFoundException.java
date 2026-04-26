package com.naman.capstone.exception;

/**
 * custom exception for restaurant not found
 */
public class RestaurantNotFoundException extends RuntimeException {

    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
