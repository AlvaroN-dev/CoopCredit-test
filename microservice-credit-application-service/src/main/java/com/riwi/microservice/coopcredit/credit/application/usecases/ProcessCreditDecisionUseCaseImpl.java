package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.CreditApplicationNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.port.in.ProcessCreditDecisionUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.CreditApplicationRepositoryPort;

public class ProcessCreditDecisionUseCaseImpl implements ProcessCreditDecisionUseCase {

    private final CreditApplicationRepositoryPort creditApplicationRepository;

    public ProcessCreditDecisionUseCaseImpl(CreditApplicationRepositoryPort creditApplicationRepository) {
        this.creditApplicationRepository = creditApplicationRepository;
    }

    @Override
    public CreditApplication startReview(Long applicationId) {
        CreditApplication application = creditApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new CreditApplicationNotFoundException(applicationId));
        
        application.startReview();
        return creditApplicationRepository.save(application);
    }

    @Override
    public CreditApplication approveCreditApplication(Long applicationId, String comments) {
        CreditApplication application = creditApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new CreditApplicationNotFoundException(applicationId));

        application.approve(comments);
        return creditApplicationRepository.save(application);
    }

    @Override
    public CreditApplication rejectCreditApplication(Long applicationId, String comments) {
        CreditApplication application = creditApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new CreditApplicationNotFoundException(applicationId));

        application.reject(comments);
        return creditApplicationRepository.save(application);
    }

    @Override
    public CreditApplication cancelCreditApplication(Long applicationId, String comments) {
        CreditApplication application = creditApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new CreditApplicationNotFoundException(applicationId));

        application.cancel(comments);
        return creditApplicationRepository.save(application);
    }
}
