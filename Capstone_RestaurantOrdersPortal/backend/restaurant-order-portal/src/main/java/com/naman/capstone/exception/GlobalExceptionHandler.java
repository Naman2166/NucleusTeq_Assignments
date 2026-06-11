package com.naman.capstone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * this class handles exception globally
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * handles UserAlreadyExistsException
     * thrown when a user already exist in the system.
     * @return status 409 CONFLICT with error message
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message",ex.getMessage()));
    }

    /**
     * handles ResourceNotFoundException
     * thrown when a resource is not found in the system
     * @return status 404 NOT_FOUND with error message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message",ex.getMessage()));
    }

    /**
     * handles CategoryAlreadyExistsException
     * thrown when a category already present in a restaurant
     * @return status 409 CONFLICT with error message
     */
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<Map<String,String>> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message",ex.getMessage()));
    }

    /**
     * handles CategoryRestaurantMismatchException
     * thrown when a category does not belong to specified restaurant
     * @return status 403 FORBIDDEN with error message
     */
    @ExceptionHandler(CategoryRestaurantMismatchException.class)
    public ResponseEntity<Map<String,String>> handleCategoryRestaurantMismatch(CategoryRestaurantMismatchException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message",ex.getMessage()));
    }

    /**
     * handles InvalidQuantityException
     * thrown when quantity of cart item is less than 1
     * @return status 400 BAD_REQUEST with error message
     */
    @ExceptionHandler(InvalidCartItemQuantityException.class)
    public ResponseEntity<Map<String, String>> handleInvalidQuantity(InvalidCartItemQuantityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }

    /**
     * handles EmptyCartException
     * thrown when user perform operation on empty cart
     * @return status 409 CONFLICT with error message
     */
    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<Map<String, String>> handleEmptyCart(EmptyCartException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
    }

    /**
     * handles InsufficientBalanceException
     * thrown when user try to spend more than wallet balance
     * @return status 409 CONFLICT with error message
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientBalance(InsufficientBalanceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
    }

    /**
     * handles OrderCancellationTimeExceededException
     * thrown when user try to cancel order after cancellation time limit
     * @return status 403 FORBIDDEN with error message
     */
    @ExceptionHandler(OrderCancellationTimeExceededException.class)
    public ResponseEntity<Map<String, String>> handleCancellationTimeExceeded(OrderCancellationTimeExceededException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", ex.getMessage()));
    }

    /**
     * handles MultipleRestaurantCartException
     * thrown when user try to add item from different restaurant in cart
     * @return status 409 CONFLICT with error message
     */
    @ExceptionHandler(MultipleRestaurantCartException.class)
    public ResponseEntity<Map<String, String>> handleMultipleRestaurantCart(MultipleRestaurantCartException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
    }

    /**
     * handles OrderNotCancellableException
     * thrown when order cannot be canceled
     * @return status 409 CONFLICT with error message
     */
    @ExceptionHandler(OrderNotCancellableException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotCancellable(OrderNotCancellableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
    }


    /**
     * handles UnauthorizedException
     * thrown when user is not authorized to perform an action
     * @return status 401 UNAUTHORIZED with error message
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String,String>> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message",ex.getMessage()));
    }

    /**
     * handles validation error
     * thrown when user input credentials are invalid
     * @return status 400 BAD_REQUEST with error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }


    /**
     * handles login credential error
     * thrown when user enters wrong login credentials
     * @return status 401 UNAUTHORIZED with error message
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String,String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Invalid email or password"));
    }


    /**
     * handles all others exceptions and returns appropriate responses
     * @return status 500 INTERNAL_SERVER_ERROR) with error message
     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String,String>> handleGlobalException(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message","Something Went Wrong"));
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {

        ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "message", "Something Went Wrong",
                        "error", ex.getMessage()
                ));
    }

}