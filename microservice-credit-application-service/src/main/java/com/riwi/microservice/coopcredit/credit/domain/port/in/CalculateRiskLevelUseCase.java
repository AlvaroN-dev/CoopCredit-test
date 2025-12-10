package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;

/**
 * Use case for calculating risk levels.
 * Single Responsibility: Only handles risk level calculation.
 */
public interface CalculateRiskLevelUseCase {
    
    /**
     * Calculate and assign risk level automatically.
     * @param evaluationId the evaluation ID
     * @return the updated risk evaluation with calculated risk level
     */
    RiskEvaluation calculateRiskLevel(Long evaluationId);
}
