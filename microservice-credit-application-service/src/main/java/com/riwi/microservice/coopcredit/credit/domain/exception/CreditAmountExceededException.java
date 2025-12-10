package com.riwi.microservice.coopcredit.credit.domain.exception;

import java.math.BigDecimal;

/**
 * Exception thrown when the requested credit amount exceeds the maximum allowed.
 */
public class CreditAmountExceededException extends DomainException {
    
    private static final String CODE = "CREDIT_AMOUNT_EXCEEDED";
    
    public CreditAmountExceededException(BigDecimal requestedAmount, BigDecimal maxAmount) {
        super(CODE, String.format(
            "El monto solicitado (%.2f) excede el máximo permitido (%.2f) basado en el salario del afiliado.",
            requestedAmount, maxAmount));
    }
}
