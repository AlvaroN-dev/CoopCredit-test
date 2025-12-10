package com.riwi.microservice.coopcredit.credit.application.services;

import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateCreditApplicationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.ProcessCreditDecisionUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.RetrieveCreditApplicationUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class CreditApplicationService implements CreateCreditApplicationUseCase, 
        RetrieveCreditApplicationUseCase, ProcessCreditDecisionUseCase {

    private final CreateCreditApplicationUseCase createCreditApplicationUseCase;
    private final RetrieveCreditApplicationUseCase retrieveCreditApplicationUseCase;
    private final ProcessCreditDecisionUseCase processCreditDecisionUseCase;

    public CreditApplicationService(
            @Qualifier("createCreditApplicationUseCaseImpl") CreateCreditApplicationUseCase createCreditApplicationUseCase,
            @Qualifier("retrieveCreditApplicationUseCaseImpl") RetrieveCreditApplicationUseCase retrieveCreditApplicationUseCase,
            @Qualifier("processCreditDecisionUseCaseImpl") ProcessCreditDecisionUseCase processCreditDecisionUseCase) {
        this.createCreditApplicationUseCase = createCreditApplicationUseCase;
        this.retrieveCreditApplicationUseCase = retrieveCreditApplicationUseCase;
        this.processCreditDecisionUseCase = processCreditDecisionUseCase;
    }

    @Override
    public CreditApplication createCreditApplication(CreateCreditApplicationCommand command) {
        return createCreditApplicationUseCase.createCreditApplication(command);
    }

    @Override
    public Optional<CreditApplication> getCreditApplicationById(Long applicationId) {
        return retrieveCreditApplicationUseCase.getCreditApplicationById(applicationId);
    }

    @Override
    public Optional<CreditApplication> getCreditApplicationByNumber(String applicationNumber) {
        return retrieveCreditApplicationUseCase.getCreditApplicationByNumber(applicationNumber);
    }

    @Override
    public List<CreditApplication> getCreditApplicationsByAffiliate(Long affiliateId) {
        return retrieveCreditApplicationUseCase.getCreditApplicationsByAffiliate(affiliateId);
    }

    @Override
    public List<CreditApplication> getCreditApplicationsByStatus(CreditApplicationStatus status, int page, int size) {
        return retrieveCreditApplicationUseCase.getCreditApplicationsByStatus(status, page, size);
    }

    @Override
    public List<CreditApplication> getAllCreditApplications(int page, int size) {
        return retrieveCreditApplicationUseCase.getAllCreditApplications(page, size);
    }

    @Override
    public Optional<CreditApplication> getCreditApplicationWithDetails(Long applicationId) {
        return retrieveCreditApplicationUseCase.getCreditApplicationWithDetails(applicationId);
    }

    @Override
    public CreditApplication startReview(Long applicationId) {
        return processCreditDecisionUseCase.startReview(applicationId);
    }

    @Override
    public CreditApplication approveCreditApplication(Long applicationId, String comments) {
        return processCreditDecisionUseCase.approveCreditApplication(applicationId, comments);
    }

    @Override
    public CreditApplication rejectCreditApplication(Long applicationId, String comments) {
        return processCreditDecisionUseCase.rejectCreditApplication(applicationId, comments);
    }

    @Override
    public CreditApplication cancelCreditApplication(Long applicationId, String comments) {
        return processCreditDecisionUseCase.cancelCreditApplication(applicationId, comments);
    }
}
