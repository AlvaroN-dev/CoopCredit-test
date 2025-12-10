package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;

import java.util.List;
import java.util.Optional;

/**
 * Use case for retrieving risk evaluations.
 * Single Responsibility: Only handles risk evaluation retrieval operations.
 */
public interface RetrieveRiskEvaluationUseCase {
    
    /**
     * Get a risk evaluation by ID.
     * @param evaluationId the evaluation ID
     * @return the risk evaluation if found
     */
    Optional<RiskEvaluation> getRiskEvaluationById(Long evaluationId);
    
    /**
     * Get a risk evaluation by credit application ID.
     * @param creditApplicationId the credit application ID
     * @return the risk evaluation if found
     */
    Optional<RiskEvaluation> getRiskEvaluationByCreditApplicationId(Long creditApplicationId);
    
    /**
     * Get all risk evaluations by risk level.
     * @param riskLevel the risk level
     * @param page the page number
     * @param size the page size
     * @return list of risk evaluations
     */
    List<RiskEvaluation> getRiskEvaluationsByRiskLevel(RiskLevel riskLevel, int page, int size);
    
    /**
     * Get all risk evaluations with pagination.
     * @param page the page number
     * @param size the page size
     * @return list of risk evaluations
     */
    List<RiskEvaluation> getAllRiskEvaluations(int page, int size);
}
