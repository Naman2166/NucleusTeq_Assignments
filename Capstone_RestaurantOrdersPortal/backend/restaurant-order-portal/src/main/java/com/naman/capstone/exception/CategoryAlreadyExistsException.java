package com.naman.capstone.exception;

/**
 * custom exception for category already exists
 */
public class CategoryAlreadyExistsException extends RuntimeException {

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}
