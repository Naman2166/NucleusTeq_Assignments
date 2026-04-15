package com.naman.assignment.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// this class handles all exceptions globally and return responses in JSON format
@RestControllerAdvice
public class GlobalExceptionHandler {

    // handles BadRequestException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());      // return 400 status and error message
    }

    //handles unexpected error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.internalServerError().body("Something went wrong");     //return 500 status with error message
    }
}