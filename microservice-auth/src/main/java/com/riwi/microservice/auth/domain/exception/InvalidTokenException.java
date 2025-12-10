package com.riwi.microservice.auth.domain.exception;

/**
 * Exception thrown when a JWT token is invalid.
 */
public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException(String message) {
        super(message);
    }
    
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
