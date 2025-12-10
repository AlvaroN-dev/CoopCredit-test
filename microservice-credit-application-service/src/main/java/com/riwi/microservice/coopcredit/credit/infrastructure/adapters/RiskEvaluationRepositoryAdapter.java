package com.riwi.microservice.coopcredit.credit.infrastructure.adapters;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskEvaluationRepositoryPort;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.CreditApplicationEntity;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.RiskEvaluationEntity;
import com.riwi.microservice.coopcredit.credit.infrastructure.mapper.RiskEvaluationEntityMapper;
import com.riwi.microservice.coopcredit.credit.infrastructure.repositories.JpaCreditApplicationRepository;
import com.riwi.microservice.coopcredit.credit.infrastructure.repositories.JpaRiskEvaluationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementing RiskEvaluationRepositoryPort using JPA.
 */
@Component
public class RiskEvaluationRepositoryAdapter implements RiskEvaluationRepositoryPort {

    private final JpaRiskEvaluationRepository jpaRepository;
    private final JpaCreditApplicationRepository creditApplicationRepository;
    private final RiskEvaluationEntityMapper mapper;

    public RiskEvaluationRepositoryAdapter(
            JpaRiskEvaluationRepository jpaRepository,
            JpaCreditApplicationRepository creditApplicationRepository,
            RiskEvaluationEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.creditApplicationRepository = creditApplicationRepository;
        this.mapper = mapper;
    }

    @Override
    public RiskEvaluation save(RiskEvaluation riskEvaluation) {
        RiskEvaluationEntity entity;

        if (riskEvaluation.getId() != null) {
            // Update existing entity
            entity = jpaRepository.findById(riskEvaluation.getId())
                    .orElseGet(() -> mapper.toEntity(riskEvaluation));
            mapper.updateEntityFromDomain(riskEvaluation, entity);
        } else {
            // Create new entity
            entity = mapper.toEntity(riskEvaluation);
        }

        // Link credit application
        if (riskEvaluation.getCreditApplication() != null && riskEvaluation.getCreditApplication().getId() != null) {
            CreditApplicationEntity creditAppEntity = creditApplicationRepository
                    .findById(riskEvaluation.getCreditApplication().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Credit application not found"));
            entity.setCreditApplication(creditAppEntity);
        }

        RiskEvaluationEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RiskEvaluation> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<RiskEvaluation> findByCreditApplicationId(Long creditApplicationId) {
        return jpaRepository.findByCreditApplicationId(creditApplicationId)
                .map(mapper::toDomain);
    }

    @Override
    public List<RiskEvaluation> findByRiskLevel(RiskLevel riskLevel, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "evaluationDate"));
        return jpaRepository.findByRiskLevel(riskLevel, pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RiskEvaluation> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "evaluationDate"));
        return jpaRepository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RiskEvaluation> findByIdWithCreditApplication(Long id) {
        return jpaRepository.findByIdWithCreditApplication(id)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByCreditApplicationId(Long creditApplicationId) {
        return jpaRepository.existsByCreditApplicationId(creditApplicationId);
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
    public long countByRiskLevel(RiskLevel riskLevel) {
        return jpaRepository.countByRiskLevel(riskLevel);
    }

    @Override
    public List<RiskEvaluation> findByEvaluatedBy(String evaluatedBy, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "evaluationDate"));
        return jpaRepository.findByEvaluatedBy(evaluatedBy, pageRequest)
                .getContent()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
