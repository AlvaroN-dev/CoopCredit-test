package com.riwi.microservice.auth.domain.exception;

/**
 * Exception thrown when a user is not found.
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}
