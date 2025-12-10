package com.riwi.microservice.coopcredit.credit.infrastructure.mapper;

import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.CreditApplicationEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between CreditApplication domain model and CreditApplicationEntity.
 */
@Component
public class CreditApplicationEntityMapper {

    private final AffiliateEntityMapper affiliateMapper;
    private final RiskEvaluationEntityMapper riskEvaluationMapper;

    public CreditApplicationEntityMapper(AffiliateEntityMapper affiliateMapper, 
                                         RiskEvaluationEntityMapper riskEvaluationMapper) {
        this.affiliateMapper = affiliateMapper;
        this.riskEvaluationMapper = riskEvaluationMapper;
    }

    /**
     * Maps CreditApplicationEntity to CreditApplication domain model.
     */
    public CreditApplication toDomain(CreditApplicationEntity entity) {
        if (entity == null) {
            return null;
        }

        CreditApplication creditApplication = new CreditApplication();
        creditApplication.setId(entity.getId());
        creditApplication.setApplicationNumber(entity.getApplicationNumber());
        creditApplication.setRequestedAmount(entity.getRequestedAmount());
        creditApplication.setTermMonths(entity.getTermMonths());
        creditApplication.setInterestRate(entity.getInterestRate());
        creditApplication.setPurpose(entity.getPurpose());
        creditApplication.setStatus(entity.getStatus());
        creditApplication.setComments(entity.getComments());
        creditApplication.setApplicationDate(entity.getApplicationDate());
        creditApplication.setReviewDate(entity.getReviewDate());
        creditApplication.setDecisionDate(entity.getDecisionDate());
        creditApplication.setCreatedAt(entity.getCreatedAt());
        creditApplication.setUpdatedAt(entity.getUpdatedAt());

        // Map affiliate if present
        if (entity.getAffiliate() != null) {
            creditApplication.setAffiliate(affiliateMapper.toDomain(entity.getAffiliate()));
        }

        // Map risk evaluation if present
        if (entity.getRiskEvaluation() != null) {
            creditApplication.setRiskEvaluation(riskEvaluationMapper.toDomainWithoutCreditApplication(entity.getRiskEvaluation()));
        }

        return creditApplication;
    }

    /**
     * Maps CreditApplication domain model to CreditApplicationEntity.
     */
    public CreditApplicationEntity toEntity(CreditApplication domain) {
        if (domain == null) {
            return null;
        }

        CreditApplicationEntity entity = new CreditApplicationEntity();
        entity.setId(domain.getId());
        entity.setApplicationNumber(domain.getApplicationNumber());
        entity.setRequestedAmount(domain.getRequestedAmount());
        entity.setTermMonths(domain.getTermMonths());
        entity.setInterestRate(domain.getInterestRate());
        entity.setPurpose(domain.getPurpose());
        entity.setStatus(domain.getStatus());
        entity.setComments(domain.getComments());
        entity.setApplicationDate(domain.getApplicationDate());
        entity.setReviewDate(domain.getReviewDate());
        entity.setDecisionDate(domain.getDecisionDate());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    /**
     * Maps without related entities to avoid circular references.
     */
    public CreditApplication toDomainWithoutRelations(CreditApplicationEntity entity) {
        if (entity == null) {
            return null;
        }

        CreditApplication creditApplication = new CreditApplication();
        creditApplication.setId(entity.getId());
        creditApplication.setApplicationNumber(entity.getApplicationNumber());
        creditApplication.setRequestedAmount(entity.getRequestedAmount());
        creditApplication.setTermMonths(entity.getTermMonths());
        creditApplication.setInterestRate(entity.getInterestRate());
        creditApplication.setPurpose(entity.getPurpose());
        creditApplication.setStatus(entity.getStatus());
        creditApplication.setComments(entity.getComments());
        creditApplication.setApplicationDate(entity.getApplicationDate());
        creditApplication.setReviewDate(entity.getReviewDate());
        creditApplication.setDecisionDate(entity.getDecisionDate());
        creditApplication.setCreatedAt(entity.getCreatedAt());
        creditApplication.setUpdatedAt(entity.getUpdatedAt());

        return creditApplication;
    }

    /**
     * Updates an existing entity from domain model.
     */
    public void updateEntityFromDomain(CreditApplication domain, CreditApplicationEntity entity) {
        if (domain == null || entity == null) {
            return;
        }

        entity.setApplicationNumber(domain.getApplicationNumber());
        entity.setRequestedAmount(domain.getRequestedAmount());
        entity.setTermMonths(domain.getTermMonths());
        entity.setInterestRate(domain.getInterestRate());
        entity.setPurpose(domain.getPurpose());
        entity.setStatus(domain.getStatus());
        entity.setComments(domain.getComments());
        entity.setApplicationDate(domain.getApplicationDate());
        entity.setReviewDate(domain.getReviewDate());
        entity.setDecisionDate(domain.getDecisionDate());
        entity.setUpdatedAt(domain.getUpdatedAt());
    }
}
