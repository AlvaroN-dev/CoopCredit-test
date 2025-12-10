package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;
import com.riwi.microservice.coopcredit.credit.domain.port.in.RetrieveRiskEvaluationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskEvaluationRepositoryPort;

import java.util.List;
import java.util.Optional;

public class RetrieveRiskEvaluationUseCaseImpl implements RetrieveRiskEvaluationUseCase {

    private final RiskEvaluationRepositoryPort riskEvaluationRepository;

    public RetrieveRiskEvaluationUseCaseImpl(RiskEvaluationRepositoryPort riskEvaluationRepository) {
        this.riskEvaluationRepository = riskEvaluationRepository;
    }

    @Override
    public Optional<RiskEvaluation> getRiskEvaluationById(Long evaluationId) {
        return riskEvaluationRepository.findById(evaluationId);
    }

    @Override
    public Optional<RiskEvaluation> getRiskEvaluationByCreditApplicationId(Long creditApplicationId) {
        return riskEvaluationRepository.findByCreditApplicationId(creditApplicationId);
    }

    @Override
    public List<RiskEvaluation> getRiskEvaluationsByRiskLevel(RiskLevel riskLevel, int page, int size) {
        return riskEvaluationRepository.findByRiskLevel(riskLevel, page, size);
    }

    @Override
    public List<RiskEvaluation> getAllRiskEvaluations(int page, int size) {
        return riskEvaluationRepository.findAll(page, size);
    }
}
