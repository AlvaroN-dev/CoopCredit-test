package com.riwi.microservice.coopcredit.credit.infrastructure.mapper;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.RiskEvaluationEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between RiskEvaluation domain model and RiskEvaluationEntity.
 */
@Component
public class RiskEvaluationEntityMapper {

    /**
     * Maps RiskEvaluationEntity to RiskEvaluation domain model.
     */
    public RiskEvaluation toDomain(RiskEvaluationEntity entity) {
        if (entity == null) {
            return null;
        }

        RiskEvaluation riskEvaluation = new RiskEvaluation();
        riskEvaluation.setId(entity.getId());
        riskEvaluation.setCreditScore(entity.getCreditScore());
        riskEvaluation.setRiskLevel(entity.getRiskLevel());
        riskEvaluation.setDebtToIncomeRatio(entity.getDebtToIncomeRatio());
        riskEvaluation.setHasDefaultHistory(entity.getHasDefaultHistory());
        riskEvaluation.setYearsEmployed(entity.getYearsEmployed());
        riskEvaluation.setHasGuarantor(entity.getHasGuarantor());
        riskEvaluation.setCollateralValue(entity.getCollateralValue());
        riskEvaluation.setEvaluationNotes(entity.getEvaluationNotes());
        riskEvaluation.setRecommendation(entity.getRecommendation());
        riskEvaluation.setApproved(entity.getApproved());
        riskEvaluation.setEvaluatedBy(entity.getEvaluatedBy());
        riskEvaluation.setEvaluationDate(entity.getEvaluationDate());
        riskEvaluation.setCreatedAt(entity.getCreatedAt());
        riskEvaluation.setUpdatedAt(entity.getUpdatedAt());

        return riskEvaluation;
    }

    /**
     * Maps without credit application to avoid circular references.
     */
    public RiskEvaluation toDomainWithoutCreditApplication(RiskEvaluationEntity entity) {
        return toDomain(entity);
    }

    /**
     * Maps RiskEvaluation domain model to RiskEvaluationEntity.
     */
    public RiskEvaluationEntity toEntity(RiskEvaluation domain) {
        if (domain == null) {
            return null;
        }

        RiskEvaluationEntity entity = new RiskEvaluationEntity();
        entity.setId(domain.getId());
        entity.setCreditScore(domain.getCreditScore());
        entity.setRiskLevel(domain.getRiskLevel());
        entity.setDebtToIncomeRatio(domain.getDebtToIncomeRatio());
        entity.setHasDefaultHistory(domain.getHasDefaultHistory());
        entity.setYearsEmployed(domain.getYearsEmployed());
        entity.setHasGuarantor(domain.getHasGuarantor());
        entity.setCollateralValue(domain.getCollateralValue());
        entity.setEvaluationNotes(domain.getEvaluationNotes());
        entity.setRecommendation(domain.getRecommendation());
        entity.setApproved(domain.getApproved());
        entity.setEvaluatedBy(domain.getEvaluatedBy());
        entity.setEvaluationDate(domain.getEvaluationDate());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    /**
     * Updates an existing entity from domain model.
     */
    public void updateEntityFromDomain(RiskEvaluation domain, RiskEvaluationEntity entity) {
        if (domain == null || entity == null) {
            return;
        }

        entity.setCreditScore(domain.getCreditScore());
        entity.setRiskLevel(domain.getRiskLevel());
        entity.setDebtToIncomeRatio(domain.getDebtToIncomeRatio());
        entity.setHasDefaultHistory(domain.getHasDefaultHistory());
        entity.setYearsEmployed(domain.getYearsEmployed());
        entity.setHasGuarantor(domain.getHasGuarantor());
        entity.setCollateralValue(domain.getCollateralValue());
        entity.setEvaluationNotes(domain.getEvaluationNotes());
        entity.setRecommendation(domain.getRecommendation());
        entity.setApproved(domain.getApproved());
        entity.setUpdatedAt(domain.getUpdatedAt());
    }
}
