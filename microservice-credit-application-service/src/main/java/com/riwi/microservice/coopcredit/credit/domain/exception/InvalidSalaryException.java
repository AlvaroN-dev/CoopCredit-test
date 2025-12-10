package com.riwi.microservice.coopcredit.credit.domain.exception;

import java.math.BigDecimal;

/**
 * Exception thrown when the salary is invalid (not positive).
 */
public class InvalidSalaryException extends DomainException {
    
    private static final String CODE = "INVALID_SALARY";
    
    public InvalidSalaryException(BigDecimal salary) {
        super(CODE, "El salario debe ser mayor a cero. Valor proporcionado: " + salary);
    }
}
