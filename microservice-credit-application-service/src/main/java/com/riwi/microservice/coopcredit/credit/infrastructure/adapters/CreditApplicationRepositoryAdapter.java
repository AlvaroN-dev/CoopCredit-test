package com.riwi.microservice.coopcredit.credit.infrastructure.adapters;

import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.AffiliateEntity;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.CreditApplicationEntity;
import com.riwi.microservice.coopcredit.credit.infrastructure.mapper.CreditApplicationEntityMapper;
import com.riwi.microservice.coopcredit.credit.infrastructure.repositories.JpaAffiliateRepository;
import com.riwi.microservice.coopcredit.credit.infrastructure.repositories.JpaCreditApplicationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementing CreditApplicationRepositoryPort using JPA.
 */
@Component
public class CreditApplicationRepositoryAdapter implements CreditApplicationRepositoryPort {

    private final JpaCreditApplicationRepository jpaRepository;
    private final JpaAffiliateRepository affiliateRepository;
    private final CreditApplicationEntityMapper mapper;

    public CreditApplicationRepositoryAdapter(
            JpaCreditApplicationRepository jpaRepository,
            JpaAffiliateRepository affiliateRepository,
            CreditApplicationEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.affiliateRepository = affiliateRepository;
        this.mapper = mapper;
    }

    @Override
    public CreditApplication save(CreditApplication creditApplication) {
        CreditApplicationEntity entity;

        if (creditApplication.getId() != null) {
            // Update existing entity
            entity = jpaRepository.findById(creditApplication.getId())
                    .orElseGet(() -> mapper.toEntity(creditApplication));
            mapper.updateEntityFromDomain(creditApplication, entity);
        } else {
            // Create new entity
            entity = mapper.toEntity(creditApplication);
        }

        // Link affiliate
        if (creditApplication.getAffiliate() != null && creditApplication.getAffiliate().getId() != null) {
            AffiliateEntity affiliateEntity = affiliateRepository.findById(creditApplication.getAffiliate().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Affiliate not found"));
            entity.setAffiliate(affiliateEntity);
        }

        CreditApplicationEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CreditApplication> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<CreditApplication> findByApplicationNumber(String applicationNumber) {
        return jpaRepository.findByApplicationNumber(applicationNumber)
                .map(mapper::toDomain);
    }

    @Override
    public List<CreditApplication> findByAffiliateId(Long affiliateId) {
        return jpaRepository.findByAffiliateId(affiliateId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditApplication> findByStatus(CreditApplicationStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "applicationDate"));
        return jpaRepository.findByStatus(status, pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditApplication> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "applicationDate"));
        return jpaRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CreditApplication> findByIdWithDetails(Long id) {
        return jpaRepository.findByIdWithDetails(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<CreditApplication> findByAffiliateIdWithRiskEvaluations(Long affiliateId) {
        return jpaRepository.findByAffiliateIdWithRiskEvaluations(affiliateId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<CreditApplication> findByApplicationDateBetween(
            LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "applicationDate"));
        return jpaRepository.findByApplicationDateBetween(startDate, endDate, pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByApplicationNumber(String applicationNumber) {
        return jpaRepository.existsByApplicationNumber(applicationNumber);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByStatus(CreditApplicationStatus status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public long countByAffiliateId(Long affiliateId) {
        return jpaRepository.countByAffiliateId(affiliateId);
    }

    @Override
    public String generateApplicationNumber() {
        String prefix = "CRE-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")) + "-";
        Long maxSequence = jpaRepository.findMaxApplicationNumberSequence(prefix);
        if (maxSequence == null) {
            maxSequence = 0L;
        }
        return prefix + String.format("%06d", maxSequence + 1);
    }
}
