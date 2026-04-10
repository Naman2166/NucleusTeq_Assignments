package com.naman.assignment.exception;

// This exception is thrown when employee is not found
public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(String message) {
        super(message);          // it pass message to parent class(ie RunTimeException)
    }
}