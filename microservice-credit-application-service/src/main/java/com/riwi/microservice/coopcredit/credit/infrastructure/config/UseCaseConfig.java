package com.riwi.microservice.coopcredit.credit.infrastructure.config;

import com.riwi.microservice.coopcredit.credit.application.usecases.*;
import com.riwi.microservice.coopcredit.credit.domain.port.in.*;
import com.riwi.microservice.coopcredit.credit.domain.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateAffiliateUseCase createAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        return new CreateAffiliateUseCaseImpl(affiliateRepository);
    }

    @Bean
    public UpdateAffiliateUseCase updateAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        return new UpdateAffiliateUseCaseImpl(affiliateRepository);
    }

    @Bean
    public RetrieveAffiliateUseCase retrieveAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        return new RetrieveAffiliateUseCaseImpl(affiliateRepository);
    }

    @Bean
    public ManageAffiliateStatusUseCase manageAffiliateStatusUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        return new ManageAffiliateStatusUseCaseImpl(affiliateRepository);
    }

    @Bean
    public DeleteAffiliateUseCase deleteAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        return new DeleteAffiliateUseCaseImpl(affiliateRepository);
    }

    @Bean
    public CreateCreditApplicationUseCase createCreditApplicationUseCaseImpl(
            CreditApplicationRepositoryPort creditApplicationRepository, 
            AffiliateRepositoryPort affiliateRepository,
            RiskAssessmentPort riskAssessmentPort,
            RiskEvaluationRepositoryPort riskEvaluationRepository) {
        return new CreateCreditApplicationUseCaseImpl(creditApplicationRepository, affiliateRepository, riskAssessmentPort, riskEvaluationRepository);
    }

    @Bean
    public RetrieveCreditApplicationUseCase retrieveCreditApplicationUseCaseImpl(CreditApplicationRepositoryPort creditApplicationRepository, AffiliateRepositoryPort affiliateRepository) {
        return new RetrieveCreditApplicationUseCaseImpl(creditApplicationRepository, affiliateRepository);
    }

    @Bean
    public ProcessCreditDecisionUseCase processCreditDecisionUseCaseImpl(CreditApplicationRepositoryPort creditApplicationRepository) {
        return new ProcessCreditDecisionUseCaseImpl(creditApplicationRepository);
    }

    @Bean
    public CreateRiskEvaluationUseCase createRiskEvaluationUseCaseImpl(RiskEvaluationRepositoryPort riskEvaluationRepository, CreditApplicationRepositoryPort creditApplicationRepository) {
        return new CreateRiskEvaluationUseCaseImpl(riskEvaluationRepository, creditApplicationRepository);
    }

    @Bean
    public UpdateRiskEvaluationUseCase updateRiskEvaluationUseCaseImpl(RiskEvaluationRepositoryPort riskEvaluationRepository) {
        return new UpdateRiskEvaluationUseCaseImpl(riskEvaluationRepository);
    }

    @Bean
    public RetrieveRiskEvaluationUseCase retrieveRiskEvaluationUseCaseImpl(RiskEvaluationRepositoryPort riskEvaluationRepository) {
        return new RetrieveRiskEvaluationUseCaseImpl(riskEvaluationRepository);
    }

    @Bean
    public CalculateRiskLevelUseCase calculateRiskLevelUseCaseImpl(RiskEvaluationRepositoryPort riskEvaluationRepository) {
        return new CalculateRiskLevelUseCaseImpl(riskEvaluationRepository);
    }
}
