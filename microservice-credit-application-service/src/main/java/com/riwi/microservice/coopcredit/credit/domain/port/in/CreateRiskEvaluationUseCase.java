package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;

import java.math.BigDecimal;

/**
 * Use case for creating risk evaluations.
 * Single Responsibility: Only handles risk evaluation creation.
 */
public interface CreateRiskEvaluationUseCase {
    
    /**
     * Create a risk evaluation for a credit application.
     * @param command the command with evaluation data
     * @return the created risk evaluation
     */
    RiskEvaluation createRiskEvaluation(CreateRiskEvaluationCommand command);
    
    /**
     * Command for creating a risk evaluation.
     */
    record CreateRiskEvaluationCommand(
            Long creditApplicationId,
            Integer creditScore,
            BigDecimal debtToIncomeRatio,
            Boolean hasDefaultHistory,
            Integer yearsEmployed,
            Boolean hasGuarantor,
            BigDecimal collateralValue,
            String evaluationNotes,
            String evaluatedBy
    ) {}
}
