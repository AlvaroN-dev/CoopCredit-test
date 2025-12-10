package com.riwi.microservice.coopcredit.credit.domain.exception;

/**
 * Base exception for all domain exceptions in the credit service.
 */
public abstract class DomainException extends RuntimeException {
    
    private final String code;
    
    protected DomainException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    protected DomainException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
}
