package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;

/**
 * Use case for processing credit application decisions.
 * Single Responsibility: Only handles credit application workflow (review, approve, reject, cancel).
 */
public interface ProcessCreditDecisionUseCase {
    
    /**
     * Start review process for a credit application.
     * @param applicationId the application ID
     * @return the updated credit application
     */
    CreditApplication startReview(Long applicationId);
    
    /**
     * Approve a credit application.
     * @param applicationId the application ID
     * @param comments approval comments
     * @return the updated credit application
     */
    CreditApplication approveCreditApplication(Long applicationId, String comments);
    
    /**
     * Reject a credit application.
     * @param applicationId the application ID
     * @param comments rejection reason
     * @return the updated credit application
     */
    CreditApplication rejectCreditApplication(Long applicationId, String comments);
    
    /**
     * Cancel a credit application.
     * @param applicationId the application ID
     * @param comments cancellation reason
     * @return the updated credit application
     */
    CreditApplication cancelCreditApplication(Long applicationId, String comments);
}
