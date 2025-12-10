package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.AffiliateNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.exception.InvalidSalaryException;
import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.port.in.UpdateAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UpdateAffiliateUseCaseImpl implements UpdateAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public UpdateAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Affiliate updateAffiliate(Long affiliateId, UpdateAffiliateCommand command) {
        Affiliate affiliate = affiliateRepository.findById(affiliateId)
                .orElseThrow(() -> new AffiliateNotFoundException(affiliateId));

        // Update fields if provided
        if (command.firstName() != null) {
            affiliate.setFirstName(command.firstName());
        }
        if (command.lastName() != null) {
            affiliate.setLastName(command.lastName());
        }
        if (command.email() != null) {
            affiliate.setEmail(command.email());
        }
        if (command.phone() != null) {
            affiliate.setPhone(command.phone());
        }
        if (command.address() != null) {
            affiliate.setAddress(command.address());
        }
        if (command.salary() != null) {
            if (command.salary().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidSalaryException(command.salary());
            }
            affiliate.setSalary(command.salary());
        }

        affiliate.setUpdatedAt(LocalDateTime.now());
        return affiliateRepository.save(affiliate);
    }
}
