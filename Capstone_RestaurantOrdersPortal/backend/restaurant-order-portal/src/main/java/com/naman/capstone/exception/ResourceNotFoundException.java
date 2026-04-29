package com.naman.capstone.exception;

/**
 * general custom exception thrown when resource is not found in system
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException (String message){
        super(message);
    }

}
