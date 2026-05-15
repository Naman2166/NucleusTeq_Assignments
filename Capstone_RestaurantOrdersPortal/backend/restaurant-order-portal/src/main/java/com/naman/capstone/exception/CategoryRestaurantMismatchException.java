package com.naman.capstone.exception;

/**
 * custom exception thrown when a category does not belong to the specified restaurant
 */
public class CategoryRestaurantMismatchException extends RuntimeException {

    public CategoryRestaurantMismatchException(String message) {
        super(message);
    }
}