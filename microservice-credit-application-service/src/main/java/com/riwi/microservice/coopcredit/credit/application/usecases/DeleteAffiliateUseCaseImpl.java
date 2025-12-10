package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.AffiliateNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.port.in.DeleteAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;

public class DeleteAffiliateUseCaseImpl implements DeleteAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public DeleteAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public void deleteAffiliate(Long affiliateId) {
        if (!affiliateRepository.existsById(affiliateId)) {
            throw new AffiliateNotFoundException(affiliateId);
        }

        // Soft delete is handled by the repository adapter using @SQLDelete
        affiliateRepository.deleteById(affiliateId);
    }
}
