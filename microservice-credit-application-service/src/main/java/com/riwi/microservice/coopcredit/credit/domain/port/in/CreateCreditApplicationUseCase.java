package com.riwi.microservice.coopcredit.credit.domain.port.in;

import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;

import java.math.BigDecimal;

/**
 * Use case for creating credit applications.
 * Single Responsibility: Only handles credit application creation.
 */
public interface CreateCreditApplicationUseCase {
    
    /**
     * Create a new credit application.
     * @param command the command with credit application data
     * @return the created credit application
     */
    CreditApplication createCreditApplication(CreateCreditApplicationCommand command);
    
    /**
     * Command for creating a credit application.
     */
    record CreateCreditApplicationCommand(
            Long affiliateId,
            BigDecimal requestedAmount,
            Integer termMonths,
            BigDecimal interestRate,
            String purpose
    ) {}
}
