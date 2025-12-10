package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.DuplicateDocumentException;
import com.riwi.microservice.coopcredit.credit.domain.exception.InvalidSalaryException;
import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateAffiliateUseCaseImpl implements CreateAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepository;

    public CreateAffiliateUseCaseImpl(AffiliateRepositoryPort affiliateRepository) {
        this.affiliateRepository = affiliateRepository;
    }

    @Override
    public Affiliate createAffiliate(CreateAffiliateCommand command) {
        // Validate unique document
        if (affiliateRepository.existsByDocument(command.document())) {
            throw new DuplicateDocumentException(command.document());
        }

        // Validate salary
        if (command.salary() == null || command.salary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSalaryException(command.salary());
        }

        // Create affiliate
        Affiliate affiliate = new Affiliate();
        affiliate.setDocument(command.document());
        affiliate.setDocumentType(command.documentType());
        affiliate.setFirstName(command.firstName());
        affiliate.setLastName(command.lastName());
        affiliate.setEmail(command.email());
        affiliate.setPhone(command.phone());
        affiliate.setBirthDate(command.birthDate());
        affiliate.setAddress(command.address());
        affiliate.setSalary(command.salary());
        affiliate.setStatus(AffiliateStatus.ACTIVO);
        affiliate.setCreatedAt(LocalDateTime.now());
        affiliate.setUpdatedAt(LocalDateTime.now());

        return affiliateRepository.save(affiliate);
    }
}
