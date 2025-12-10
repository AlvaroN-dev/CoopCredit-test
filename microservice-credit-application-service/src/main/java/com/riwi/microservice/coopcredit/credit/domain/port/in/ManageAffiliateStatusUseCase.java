package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;

/**
 * Use case for managing affiliate status.
 * Single Responsibility: Only handles affiliate status changes.
 */
public interface ManageAffiliateStatusUseCase {
    
    /**
     * Change affiliate status.
     * @param affiliateId the affiliate ID
     * @param newStatus the new status
     * @return the updated affiliate
     */
    Affiliate changeAffiliateStatus(Long affiliateId, AffiliateStatus newStatus);
}
