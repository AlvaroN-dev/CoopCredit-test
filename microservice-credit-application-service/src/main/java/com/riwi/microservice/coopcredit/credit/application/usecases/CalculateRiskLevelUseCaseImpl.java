package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.RiskEvaluationNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CalculateRiskLevelUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskEvaluationRepositoryPort;

public class CalculateRiskLevelUseCaseImpl implements CalculateRiskLevelUseCase {

    private final RiskEvaluationRepositoryPort riskEvaluationRepository;

    public CalculateRiskLevelUseCaseImpl(RiskEvaluationRepositoryPort riskEvaluationRepository) {
        this.riskEvaluationRepository = riskEvaluationRepository;
    }

    @Override
    public RiskEvaluation calculateRiskLevel(Long evaluationId) {
        RiskEvaluation riskEvaluation = riskEvaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RiskEvaluationNotFoundException(evaluationId));

        riskEvaluation.setRiskLevel(riskEvaluation.calculateRiskLevelFromScore());
        
        return riskEvaluationRepository.save(riskEvaluation);
    }
}
