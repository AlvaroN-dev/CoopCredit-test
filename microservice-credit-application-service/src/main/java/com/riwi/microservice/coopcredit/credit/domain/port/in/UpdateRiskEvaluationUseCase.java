package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;

import java.math.BigDecimal;

/**
 * Use case for updating risk evaluations.
 * Single Responsibility: Only handles risk evaluation updates.
 */
public interface UpdateRiskEvaluationUseCase {
    
    /**
     * Update an existing risk evaluation.
     * @param evaluationId the evaluation ID
     * @param command the command with updated data
     * @return the updated risk evaluation
     */
    RiskEvaluation updateRiskEvaluation(Long evaluationId, UpdateRiskEvaluationCommand command);
    
    /**
     * Command for updating a risk evaluation.
     */
    record UpdateRiskEvaluationCommand(
            Integer creditScore,
            BigDecimal debtToIncomeRatio,
            Boolean hasDefaultHistory,
            Integer yearsEmployed,
            Boolean hasGuarantor,
            BigDecimal collateralValue,
            String evaluationNotes,
            Boolean approved
    ) {}
}
