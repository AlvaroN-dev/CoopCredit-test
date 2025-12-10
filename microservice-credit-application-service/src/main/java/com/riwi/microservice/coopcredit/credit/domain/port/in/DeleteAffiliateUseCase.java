package com.riwi.microservice.coopcredit.credit.domain.port.in;

/**
 * Use case for deleting affiliates.
 * Single Responsibility: Only handles affiliate deletion.
 */
public interface DeleteAffiliateUseCase {
    
    /**
     * Delete an affiliate (soft delete by changing status to RETIRADO).
     * @param affiliateId the affiliate ID
     */
    void deleteAffiliate(Long affiliateId);
}
