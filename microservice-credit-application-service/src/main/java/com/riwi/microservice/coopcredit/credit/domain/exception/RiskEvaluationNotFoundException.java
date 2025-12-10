package com.riwi.microservice.coopcredit.credit.domain.exception;

/**
 * Exception thrown when a risk evaluation is not found.
 */
public class RiskEvaluationNotFoundException extends DomainException {
    
    private static final String CODE = "RISK_EVALUATION_NOT_FOUND";
    
    public RiskEvaluationNotFoundException(Long evaluationId) {
        super(CODE, "Evaluación de riesgo no encontrada con ID: " + evaluationId);
    }
    
    public RiskEvaluationNotFoundException(Long creditApplicationId, boolean byCreditApplication) {
        super(CODE, "Evaluación de riesgo no encontrada para la solicitud de crédito con ID: " + creditApplicationId);
    }
}
