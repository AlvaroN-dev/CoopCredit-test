package com.riwi.microservice.coopcredit.credit.application.mapper;

import com.riwi.microservice.coopcredit.credit.application.dto.risk.CreateRiskEvaluationRequest;
import com.riwi.microservice.coopcredit.credit.application.dto.risk.RiskEvaluationResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.risk.UpdateRiskEvaluationRequest;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateRiskEvaluationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.UpdateRiskEvaluationUseCase;
import org.springframework.stereotype.Component;

/**
 * Mapper for RiskEvaluation domain model and DTOs.
 */
@Component
public class RiskEvaluationMapper {

    /**
     * Maps a CreateRiskEvaluationRequest to CreateRiskEvaluationCommand.
     */
    public CreateRiskEvaluationUseCase.CreateRiskEvaluationCommand toCreateCommand(CreateRiskEvaluationRequest request) {
        return new CreateRiskEvaluationUseCase.CreateRiskEvaluationCommand(
                request.getCreditApplicationId(),
                request.getCreditScore(),
                request.getDebtToIncomeRatio(),
                request.getHasDefaultHistory(),
                request.getYearsEmployed(),
                request.getHasGuarantor(),
                request.getCollateralValue(),
                request.getEvaluationNotes(),
                request.getEvaluatedBy()
        );
    }

    /**
     * Maps an UpdateRiskEvaluationRequest to UpdateRiskEvaluationCommand.
     */
    public UpdateRiskEvaluationUseCase.UpdateRiskEvaluationCommand toUpdateCommand(UpdateRiskEvaluationRequest request) {
        return new UpdateRiskEvaluationUseCase.UpdateRiskEvaluationCommand(
                request.getCreditScore(),
                request.getDebtToIncomeRatio(),
                request.getHasDefaultHistory(),
                request.getYearsEmployed(),
                request.getHasGuarantor(),
                request.getCollateralValue(),
                request.getEvaluationNotes(),
                request.getApproved()
        );
    }

    /**
     * Maps a RiskEvaluation domain model to RiskEvaluationResponse DTO.
     */
    public RiskEvaluationResponse toResponse(RiskEvaluation riskEvaluation) {
        if (riskEvaluation == null) {
            return null;
        }

        RiskEvaluationResponse response = new RiskEvaluationResponse();
        response.setId(riskEvaluation.getId());
        response.setCreditScore(riskEvaluation.getCreditScore());
        response.setRiskLevel(riskEvaluation.getRiskLevel());
        response.setDebtToIncomeRatio(riskEvaluation.getDebtToIncomeRatio());
        response.setHasDefaultHistory(riskEvaluation.getHasDefaultHistory());
        response.setYearsEmployed(riskEvaluation.getYearsEmployed());
        response.setHasGuarantor(riskEvaluation.getHasGuarantor());
        response.setCollateralValue(riskEvaluation.getCollateralValue());
        response.setEvaluationNotes(riskEvaluation.getEvaluationNotes());
        response.setRecommendation(riskEvaluation.getRecommendation());
        response.setApproved(riskEvaluation.getApproved());
        response.setEvaluatedBy(riskEvaluation.getEvaluatedBy());
        response.setEvaluationDate(riskEvaluation.getEvaluationDate());
        response.setCreatedAt(riskEvaluation.getCreatedAt());
        response.setUpdatedAt(riskEvaluation.getUpdatedAt());
        response.setCompositeRiskScore(riskEvaluation.calculateCompositeRiskScore());

        return response;
    }
}
