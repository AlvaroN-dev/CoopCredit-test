package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.AffiliateNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.in.ManageAffiliateStatusUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;

import java.time.LocalDateTime;

public class ManageAffiliateStatusUseCaseImpl implements ManageAffiliateStatusUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public ManageAffiliateStatusUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Affiliate changeAffiliateStatus(Long affiliateId, AffiliateStatus newStatus) {
        Affiliate affiliate = affiliateRepository.findById(affiliateId)
                .orElseThrow(() -> new AffiliateNotFoundException(affiliateId));

        affiliate.setStatus(newStatus);
        affiliate.setUpdatedAt(LocalDateTime.now());
        return affiliateRepository.save(affiliate);
    }
}
