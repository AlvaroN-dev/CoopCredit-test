 package com.riwi.microservice.auth.domain.exception;

/**
 * Exception thrown when trying to create a user that already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
