package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.AffiliateNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.in.RetrieveCreditApplicationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.microservice.coopcredit.credit.domain.port.out.CreditApplicationRepositoryPort;

import java.util.List;
import java.util.Optional;

public class RetrieveCreditApplicationUseCaseImpl implements RetrieveCreditApplicationUseCase {

    private final CreditApplicationRepositoryPort creditApplicationRepository;
    private final AffiliateRepositoryPort affiliateRepository;

    public RetrieveCreditApplicationUseCaseImpl(CreditApplicationRepositoryPort creditApplicationRepository, AffiliateRepositoryPort affiliateRepository) {
        this.creditApplicationRepository = creditApplicationRepository;
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Optional<CreditApplication> getCreditApplicationById(Long applicationId) {
        return creditApplicationRepository.findById(applicationId);
    }

    @Override
    public Optional<CreditApplication> getCreditApplicationByNumber(String applicationNumber) {
        return creditApplicationRepository.findByApplicationNumber(applicationNumber);
    }

    @Override
    public List<CreditApplication> getCreditApplicationsByAffiliate(Long affiliateId) {
        // Validate affiliate exists
        if (affiliateRepository.findById(affiliateId).isEmpty()) {
            throw new AffiliateNotFoundException(affiliateId);
        }
        return creditApplicationRepository.findByAffiliateId(affiliateId);
    }

    @Override
    public List<CreditApplication> getCreditApplicationsByStatus(CreditApplicationStatus status, int page, int size) {
        return creditApplicationRepository.findByStatus(status, page, size);
    }

    @Override
    public List<CreditApplication> getAllCreditApplications(int page, int size) {
        return creditApplicationRepository.findAll(page, size);
    }

    @Override
    public Optional<CreditApplication> getCreditApplicationWithDetails(Long applicationId) {
        return creditApplicationRepository.findByIdWithDetails(applicationId);
    }
}
