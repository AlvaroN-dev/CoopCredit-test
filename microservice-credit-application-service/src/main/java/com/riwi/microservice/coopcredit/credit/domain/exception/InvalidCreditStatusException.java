package com.riwi.microservice.coopcredit.credit.domain.exception;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;

/**
 * Exception thrown when a credit application has an invalid status for the requested operation.
 */
public class InvalidCreditStatusException extends DomainException {
    
    private static final String CODE = "INVALID_CREDIT_STATUS";
    
    public InvalidCreditStatusException(Long applicationId, CreditApplicationStatus currentStatus, String operation) {
        super(CODE, String.format(
            "No se puede realizar la operación '%s' en la solicitud %d con estado '%s'.",
            operation, applicationId, currentStatus));
    }
}
