package com.riwi.microservice.coopcredit.credit.application.mapper;

import com.riwi.microservice.coopcredit.credit.application.dto.affiliate.AffiliateResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.affiliate.CreateAffiliateRequest;
import com.riwi.microservice.coopcredit.credit.application.dto.affiliate.UpdateAffiliateRequest;
import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.UpdateAffiliateUseCase;
import org.springframework.stereotype.Component;

/**
 * Mapper for Affiliate domain model and DTOs.
 */
@Component
public class AffiliateMapper {

    /**
     * Maps a CreateAffiliateRequest to CreateAffiliateCommand.
     */
    public CreateAffiliateUseCase.CreateAffiliateCommand toCreateCommand(CreateAffiliateRequest request) {
        return new CreateAffiliateUseCase.CreateAffiliateCommand(
                request.getDocument(),
                request.getDocumentType(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getBirthDate(),
                request.getAddress(),
                request.getSalary()
        );
    }

    /**
     * Maps an UpdateAffiliateRequest to UpdateAffiliateCommand.
     */
    public UpdateAffiliateUseCase.UpdateAffiliateCommand toUpdateCommand(UpdateAffiliateRequest request) {
        return new UpdateAffiliateUseCase.UpdateAffiliateCommand(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getAddress(),
                request.getSalary()
        );
    }

    /**
     * Maps an Affiliate domain model to AffiliateResponse DTO.
     */
    public AffiliateResponse toResponse(Affiliate affiliate) {
        if (affiliate == null) {
            return null;
        }

        AffiliateResponse response = new AffiliateResponse();
        response.setId(affiliate.getId());
        response.setDocument(affiliate.getDocument());
        response.setDocumentType(affiliate.getDocumentType());
        response.setFirstName(affiliate.getFirstName());
        response.setLastName(affiliate.getLastName());
        response.setFullName(affiliate.getFullName());
        response.setEmail(affiliate.getEmail());
        response.setPhone(affiliate.getPhone());
        response.setBirthDate(affiliate.getBirthDate());
        response.setAddress(affiliate.getAddress());
        response.setSalary(affiliate.getSalary());
        response.setMaxCreditAmount(affiliate.calculateMaxCreditAmount());
        response.setStatus(affiliate.getStatus());
        response.setCreatedAt(affiliate.getCreatedAt());
        response.setUpdatedAt(affiliate.getUpdatedAt());

        return response;
    }
}
