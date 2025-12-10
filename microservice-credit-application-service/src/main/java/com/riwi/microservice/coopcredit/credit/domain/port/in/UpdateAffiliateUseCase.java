package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;

import java.math.BigDecimal;

/**
 * Use case for updating affiliates.
 * Single Responsibility: Only handles affiliate updates.
 */
public interface UpdateAffiliateUseCase {
    
    /**
     * Update an existing affiliate.
     * @param affiliateId the affiliate ID
     * @param command the command with updated data
     * @return the updated affiliate
     */
    Affiliate updateAffiliate(Long affiliateId, UpdateAffiliateCommand command);
    
    /**
     * Command for updating an affiliate.
     */
    record UpdateAffiliateCommand(
            String firstName,
            String lastName,
            String email,
            String phone,
            String address,
            BigDecimal salary
    ) {}
}
