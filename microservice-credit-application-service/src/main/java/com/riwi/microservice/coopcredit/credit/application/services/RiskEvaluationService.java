package com.riwi.microservice.coopcredit.credit.application.services;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CalculateRiskLevelUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateRiskEvaluationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.RetrieveRiskEvaluationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.UpdateRiskEvaluationUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class RiskEvaluationService implements CreateRiskEvaluationUseCase, UpdateRiskEvaluationUseCase,
        RetrieveRiskEvaluationUseCase, CalculateRiskLevelUseCase {

    private final CreateRiskEvaluationUseCase createRiskEvaluationUseCase;
    private final UpdateRiskEvaluationUseCase updateRiskEvaluationUseCase;
    private final RetrieveRiskEvaluationUseCase retrieveRiskEvaluationUseCase;
    private final CalculateRiskLevelUseCase calculateRiskLevelUseCase;

    public RiskEvaluationService(
            @Qualifier("createRiskEvaluationUseCaseImpl") CreateRiskEvaluationUseCase createRiskEvaluationUseCase,
            @Qualifier("updateRiskEvaluationUseCaseImpl") UpdateRiskEvaluationUseCase updateRiskEvaluationUseCase,
            @Qualifier("retrieveRiskEvaluationUseCaseImpl") RetrieveRiskEvaluationUseCase retrieveRiskEvaluationUseCase,
            @Qualifier("calculateRiskLevelUseCaseImpl") CalculateRiskLevelUseCase calculateRiskLevelUseCase) {
        this.createRiskEvaluationUseCase = createRiskEvaluationUseCase;
        this.updateRiskEvaluationUseCase = updateRiskEvaluationUseCase;
        this.retrieveRiskEvaluationUseCase = retrieveRiskEvaluationUseCase;
        this.calculateRiskLevelUseCase = calculateRiskLevelUseCase;
    }

    @Override
    public RiskEvaluation createRiskEvaluation(CreateRiskEvaluationCommand command) {
        return createRiskEvaluationUseCase.createRiskEvaluation(command);
    }

    @Override
    public RiskEvaluation updateRiskEvaluation(Long evaluationId, UpdateRiskEvaluationCommand command) {
        return updateRiskEvaluationUseCase.updateRiskEvaluation(evaluationId, command);
    }

    @Override
    public Optional<RiskEvaluation> getRiskEvaluationById(Long evaluationId) {
        return retrieveRiskEvaluationUseCase.getRiskEvaluationById(evaluationId);
    }

    @Override
    public Optional<RiskEvaluation> getRiskEvaluationByCreditApplicationId(Long creditApplicationId) {
        return retrieveRiskEvaluationUseCase.getRiskEvaluationByCreditApplicationId(creditApplicationId);
    }

    @Override
    public List<RiskEvaluation> getRiskEvaluationsByRiskLevel(RiskLevel riskLevel, int page, int size) {
        return retrieveRiskEvaluationUseCase.getRiskEvaluationsByRiskLevel(riskLevel, page, size);
    }

    @Override
    public List<RiskEvaluation> getAllRiskEvaluations(int page, int size) {
        return retrieveRiskEvaluationUseCase.getAllRiskEvaluations(page, size);
    }

    @Override
    public RiskEvaluation calculateRiskLevel(Long evaluationId) {
        return calculateRiskLevelUseCase.calculateRiskLevel(evaluationId);
    }
}
