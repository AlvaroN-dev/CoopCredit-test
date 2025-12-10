package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.in.RetrieveAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;

import java.util.List;
import java.util.Optional;

public class RetrieveAffiliateUseCaseImpl implements RetrieveAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public RetrieveAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Optional<Affiliate> getAffiliateById(Long affiliateId) {
        return affiliateRepository.findById(affiliateId);
    }

    @Override
    public Optional<Affiliate> getAffiliateByDocument(String document) {
        return affiliateRepository.findByDocument(document);
    }

    @Override
    public List<Affiliate> getAllAffiliates(int page, int size) {
        return affiliateRepository.findAll(page, size);
    }

    @Override
    public List<Affiliate> getAffiliatesByStatus(AffiliateStatus status, int page, int size) {
        return affiliateRepository.findByStatus(status, page, size);
    }
}
