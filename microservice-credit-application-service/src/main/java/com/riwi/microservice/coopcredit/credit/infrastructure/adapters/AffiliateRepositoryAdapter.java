package com.riwi.microservice.coopcredit.credit.infrastructure.adapters;

import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.AffiliateEntity;
import com.riwi.microservice.coopcredit.credit.infrastructure.mapper.AffiliateEntityMapper;
import com.riwi.microservice.coopcredit.credit.infrastructure.repositories.JpaAffiliateRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementing AffiliateRepositoryPort using JPA.
 */
@Component
public class AffiliateRepositoryAdapter implements AffiliateRepositoryPort {

    private final JpaAffiliateRepository jpaRepository;
    private final AffiliateEntityMapper mapper;

    public AffiliateRepositoryAdapter(JpaAffiliateRepository jpaRepository, AffiliateEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Affiliate save(Affiliate affiliate) {
        AffiliateEntity entity;
        
        if (affiliate.getId() != null) {
            // Update existing entity
            entity = jpaRepository.findById(affiliate.getId())
                    .orElseGet(() -> mapper.toEntity(affiliate));
            mapper.updateEntityFromDomain(affiliate, entity);
        } else {
            // Create new entity
            entity = mapper.toEntity(affiliate);
        }

        AffiliateEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Affiliate> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Affiliate> findByDocument(String document) {
        return jpaRepository.findByDocument(document)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Affiliate> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByDocument(String document) {
        return jpaRepository.existsByDocument(document);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public List<Affiliate> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return jpaRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Affiliate> findByStatus(AffiliateStatus status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return jpaRepository.findByStatus(status, pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Affiliate> findByIdWithCreditApplications(Long id) {
        return jpaRepository.findByIdWithCreditApplications(id)
                .map(mapper::toDomain);
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
    public long countByStatus(AffiliateStatus status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
