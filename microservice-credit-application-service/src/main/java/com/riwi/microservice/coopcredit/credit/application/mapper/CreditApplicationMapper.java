package com.riwi.microservice.coopcredit.credit.application.mapper;

import com.riwi.microservice.coopcredit.credit.application.dto.credit.CreditApplicationResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.credit.CreateCreditApplicationRequest;
import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateCreditApplicationUseCase;
import org.springframework.stereotype.Component;

/**
 * Mapper for CreditApplication domain model and DTOs.
 */
@Component
public class CreditApplicationMapper {

    private final AffiliateMapper affiliateMapper;
    private final RiskEvaluationMapper riskEvaluationMapper;

    public CreditApplicationMapper(AffiliateMapper affiliateMapper, RiskEvaluationMapper riskEvaluationMapper) {
        this.affiliateMapper = affiliateMapper;
        this.riskEvaluationMapper = riskEvaluationMapper;
    }

    /**
     * Maps a CreateCreditApplicationRequest to CreateCreditApplicationCommand.
     */
    public CreateCreditApplicationUseCase.CreateCreditApplicationCommand toCreateCommand(CreateCreditApplicationRequest request) {
        return new CreateCreditApplicationUseCase.CreateCreditApplicationCommand(
                request.getAffiliateId(),
                request.getRequestedAmount(),
                request.getTermMonths(),
                request.getInterestRate(),
                request.getPurpose()
        );
    }

    /**
     * Maps a CreditApplication domain model to CreditApplicationResponse DTO.
     */
    public CreditApplicationResponse toResponse(CreditApplication creditApplication) {
        if (creditApplication == null) {
            return null;
        }

        CreditApplicationResponse response = new CreditApplicationResponse();
        response.setId(creditApplication.getId());
        response.setApplicationNumber(creditApplication.getApplicationNumber());
        response.setRequestedAmount(creditApplication.getRequestedAmount());
        response.setTermMonths(creditApplication.getTermMonths());
        response.setInterestRate(creditApplication.getInterestRate());
        response.setMonthlyPayment(creditApplication.calculateMonthlyPayment());
        response.setPurpose(creditApplication.getPurpose());
        response.setStatus(creditApplication.getStatus());
        response.setComments(creditApplication.getComments());
        response.setApplicationDate(creditApplication.getApplicationDate());
        response.setReviewDate(creditApplication.getReviewDate());
        response.setDecisionDate(creditApplication.getDecisionDate());
        response.setCreatedAt(creditApplication.getCreatedAt());
        response.setUpdatedAt(creditApplication.getUpdatedAt());

        // Map related entities if present
        if (creditApplication.getAffiliate() != null) {
            response.setAffiliate(affiliateMapper.toResponse(creditApplication.getAffiliate()));
        }
        if (creditApplication.getRiskEvaluation() != null) {
            response.setRiskEvaluation(riskEvaluationMapper.toResponse(creditApplication.getRiskEvaluation()));
        }

        return response;
    }

    /**
     * Maps a CreditApplication domain model to CreditApplicationResponse DTO without related entities.
     */
    public CreditApplicationResponse toSimpleResponse(CreditApplication creditApplication) {
        if (creditApplication == null) {
            return null;
        }

        CreditApplicationResponse response = new CreditApplicationResponse();
        response.setId(creditApplication.getId());
        response.setApplicationNumber(creditApplication.getApplicationNumber());
        response.setRequestedAmount(creditApplication.getRequestedAmount());
        response.setTermMonths(creditApplication.getTermMonths());
        response.setInterestRate(creditApplication.getInterestRate());
        response.setMonthlyPayment(creditApplication.calculateMonthlyPayment());
        response.setPurpose(creditApplication.getPurpose());
        response.setStatus(creditApplication.getStatus());
        response.setComments(creditApplication.getComments());
        response.setApplicationDate(creditApplication.getApplicationDate());
        response.setReviewDate(creditApplication.getReviewDate());
        response.setDecisionDate(creditApplication.getDecisionDate());
        response.setCreatedAt(creditApplication.getCreatedAt());
        response.setUpdatedAt(creditApplication.getUpdatedAt());

        return response;
    }
}
