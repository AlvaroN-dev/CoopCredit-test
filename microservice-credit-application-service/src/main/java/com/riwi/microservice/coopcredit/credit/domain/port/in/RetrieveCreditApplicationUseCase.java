package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;

import java.util.List;
import java.util.Optional;

/**
 * Use case for retrieving credit applications.
 * Single Responsibility: Only handles credit application retrieval operations.
 */
public interface RetrieveCreditApplicationUseCase {
    
    /**
     * Get a credit application by ID.
     * @param applicationId the application ID
     * @return the credit application if found
     */
    Optional<CreditApplication> getCreditApplicationById(Long applicationId);
    
    /**
     * Get a credit application by application number.
     * @param applicationNumber the application number
     * @return the credit application if found
     */
    Optional<CreditApplication> getCreditApplicationByNumber(String applicationNumber);
    
    /**
     * Get credit applications by affiliate ID.
     * @param affiliateId the affiliate ID
     * @return list of credit applications for the affiliate
     */
    List<CreditApplication> getCreditApplicationsByAffiliate(Long affiliateId);
    
    /**
     * Get credit applications by status with pagination.
     * @param status the application status
     * @param page the page number
     * @param size the page size
     * @return list of credit applications with the given status
     */
    List<CreditApplication> getCreditApplicationsByStatus(CreditApplicationStatus status, int page, int size);
    
    /**
     * Get all credit applications with pagination.
     * @param page the page number
     * @param size the page size
     * @return list of credit applications
     */
    List<CreditApplication> getAllCreditApplications(int page, int size);
    
    /**
     * Get credit application with full details (including affiliate and risk evaluation).
     * @param applicationId the application ID
     * @return the credit application with all related data
     */
    Optional<CreditApplication> getCreditApplicationWithDetails(Long applicationId);
}
