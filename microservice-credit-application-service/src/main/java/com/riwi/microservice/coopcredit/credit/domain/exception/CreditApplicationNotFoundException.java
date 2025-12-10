package com.riwi.microservice.coopcredit.credit.domain.exception;

/**
 * Exception thrown when a credit application is not found.
 */
public class CreditApplicationNotFoundException extends DomainException {
    
    private static final String CODE = "CREDIT_APPLICATION_NOT_FOUND";
    
    public CreditApplicationNotFoundException(Long applicationId) {
        super(CODE, "Solicitud de crédito no encontrada con ID: " + applicationId);
    }
    
    public CreditApplicationNotFoundException(String applicationNumber) {
        super(CODE, "Solicitud de crédito no encontrada con número: " + applicationNumber);
    }
}
