package com.naman.assignment.exception;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


// This class handles all exceptions in one place and returns simple messages
@RestControllerAdvice                    // handles exceptions for all controllers
public class GlobalExceptionHandler {

    // method to handle EmployeeNotFoundException
    @ExceptionHandler(EmployeeNotFoundException.class)              //if exception is thrown by EmployeeNotFoundException class, run this method
    public String handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return ex.getMessage();     // return custom error message
    }


    // method to handle InvalidRequestException
    @ExceptionHandler(InvalidRequestException.class)
    public String handleInvalidRequest(InvalidRequestException ex) {
        return ex.getMessage();    // return custom error message
    }


    // method to handle validation errors (like empty fields or invalid email)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();       // map stores field name and its error message

        // loop through all field errors
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {

            // add field name and its message to map
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return errors;     // return all errors
    }
}