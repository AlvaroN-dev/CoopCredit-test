package com.riwi.microservice.coopcredit.credit.domain.exception;

/**
 * Exception thrown when a risk evaluation already exists for a credit application.
 */
public class RiskEvaluationAlreadyExistsException extends DomainException {
    
    private static final String CODE = "RISK_EVALUATION_ALREADY_EXISTS";
    
    public RiskEvaluationAlreadyExistsException(Long creditApplicationId) {
        super(CODE, "Ya existe una evaluación de riesgo para la solicitud de crédito con ID: " + creditApplicationId);
    }
}
