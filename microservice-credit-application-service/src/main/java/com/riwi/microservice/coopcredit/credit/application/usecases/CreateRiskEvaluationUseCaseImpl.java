package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.CreditApplicationNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateRiskEvaluationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskEvaluationRepositoryPort;

import java.time.LocalDateTime;

public class CreateRiskEvaluationUseCaseImpl implements CreateRiskEvaluationUseCase {

    private final RiskEvaluationRepositoryPort riskEvaluationRepository;
    private final CreditApplicationRepositoryPort creditApplicationRepository;

    public CreateRiskEvaluationUseCaseImpl(RiskEvaluationRepositoryPort riskEvaluationRepository, CreditApplicationRepositoryPort creditApplicationRepository) {
        this.riskEvaluationRepository = riskEvaluationRepository;
        this.creditApplicationRepository = creditApplicationRepository;
    }

    @Override
    public RiskEvaluation createRiskEvaluation(CreateRiskEvaluationCommand command) {
        CreditApplication creditApplication = creditApplicationRepository.findById(command.creditApplicationId())
                .orElseThrow(() -> new CreditApplicationNotFoundException(command.creditApplicationId()));

        RiskEvaluation riskEvaluation = new RiskEvaluation();
        riskEvaluation.setCreditApplication(creditApplication);
        riskEvaluation.setCreditScore(command.creditScore());
        riskEvaluation.setDebtToIncomeRatio(command.debtToIncomeRatio());
        riskEvaluation.setHasDefaultHistory(command.hasDefaultHistory());
        riskEvaluation.setYearsEmployed(command.yearsEmployed());
        riskEvaluation.setHasGuarantor(command.hasGuarantor());
        riskEvaluation.setCollateralValue(command.collateralValue());
        riskEvaluation.setEvaluationNotes(command.evaluationNotes());
        riskEvaluation.setEvaluatedBy(command.evaluatedBy());
        riskEvaluation.setEvaluationDate(LocalDateTime.now());
        
        // Calculate initial risk level
        riskEvaluation.setRiskLevel(riskEvaluation.calculateRiskLevelFromScore());

        return riskEvaluationRepository.save(riskEvaluation);
    }
}
