package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;

import java.util.List;
import java.util.Optional;

/**
 * Use case for retrieving affiliates.
 * Single Responsibility: Only handles affiliate retrieval operations.
 */
public interface RetrieveAffiliateUseCase {
    
    /**
     * Get an affiliate by ID.
     * @param affiliateId the affiliate ID
     * @return the affiliate if found
     */
    Optional<Affiliate> getAffiliateById(Long affiliateId);
    
    /**
     * Get an affiliate by document.
     * @param document the document number
     * @return the affiliate if found
     */
    Optional<Affiliate> getAffiliateByDocument(String document);
    
    /**
     * Get all affiliates with pagination.
     * @param page the page number
     * @param size the page size
     * @return list of affiliates
     */
    List<Affiliate> getAllAffiliates(int page, int size);
    
    /**
     * Get affiliates by status.
     * @param status the affiliate status
     * @param page the page number
     * @param size the page size
     * @return list of affiliates with the given status
     */
    List<Affiliate> getAffiliatesByStatus(AffiliateStatus status, int page, int size);
}
