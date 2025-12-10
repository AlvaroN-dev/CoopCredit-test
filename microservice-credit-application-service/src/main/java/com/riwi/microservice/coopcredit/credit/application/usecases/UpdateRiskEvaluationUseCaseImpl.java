package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.RiskEvaluationNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.port.in.UpdateRiskEvaluationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskEvaluationRepositoryPort;

public class UpdateRiskEvaluationUseCaseImpl implements UpdateRiskEvaluationUseCase {

    private final RiskEvaluationRepositoryPort riskEvaluationRepository;

    public UpdateRiskEvaluationUseCaseImpl(RiskEvaluationRepositoryPort riskEvaluationRepository) {
        this.riskEvaluationRepository = riskEvaluationRepository;
    }

    @Override
    public RiskEvaluation updateRiskEvaluation(Long evaluationId, UpdateRiskEvaluationCommand command) {
        RiskEvaluation riskEvaluation = riskEvaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RiskEvaluationNotFoundException(evaluationId));

        if (command.creditScore() != null) riskEvaluation.setCreditScore(command.creditScore());
        if (command.debtToIncomeRatio() != null) riskEvaluation.setDebtToIncomeRatio(command.debtToIncomeRatio());
        if (command.hasDefaultHistory() != null) riskEvaluation.setHasDefaultHistory(command.hasDefaultHistory());
        if (command.yearsEmployed() != null) riskEvaluation.setYearsEmployed(command.yearsEmployed());
        if (command.hasGuarantor() != null) riskEvaluation.setHasGuarantor(command.hasGuarantor());
        if (command.collateralValue() != null) riskEvaluation.setCollateralValue(command.collateralValue());
        if (command.evaluationNotes() != null) riskEvaluation.setEvaluationNotes(command.evaluationNotes());
        if (command.approved() != null) riskEvaluation.setApproved(command.approved());

        // Recalculate risk level if score changed
        if (command.creditScore() != null) {
            riskEvaluation.setRiskLevel(riskEvaluation.calculateRiskLevelFromScore());
        }

        return riskEvaluationRepository.save(riskEvaluation);
    }
}
