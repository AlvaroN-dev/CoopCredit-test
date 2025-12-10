package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Use case for creating affiliates.
 * Single Responsibility: Only handles affiliate creation.
 */
public interface CreateAffiliateUseCase {
    
    /**
     * Create a new affiliate.
     * @param command the command with affiliate data
     * @return the created affiliate
     */
    Affiliate createAffiliate(CreateAffiliateCommand command);
    
    /**
     * Command for creating an affiliate.
     */
    record CreateAffiliateCommand(
            String document,
            String documentType,
            String firstName,
            String lastName,
            String email,
            String phone,
            LocalDate birthDate,
            String address,
            BigDecimal salary
    ) {}
}
