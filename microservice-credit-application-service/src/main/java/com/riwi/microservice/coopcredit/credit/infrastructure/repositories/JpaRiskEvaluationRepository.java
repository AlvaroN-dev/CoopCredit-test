package com.riwi.microservice.coopcredit.credit.infrastructure.repositories;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.RiskEvaluationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for RiskEvaluationEntity.
 */
@Repository
public interface JpaRiskEvaluationRepository extends JpaRepository<RiskEvaluationEntity, Long> {

    Optional<RiskEvaluationEntity> findByCreditApplicationId(Long creditApplicationId);

    boolean existsByCreditApplicationId(Long creditApplicationId);

    Page<RiskEvaluationEntity> findByRiskLevel(RiskLevel riskLevel, Pageable pageable);

    Page<RiskEvaluationEntity> findByEvaluatedBy(String evaluatedBy, Pageable pageable);

    long countByRiskLevel(RiskLevel riskLevel);

    @EntityGraph(value = "RiskEvaluation.withCreditApplication", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT r FROM RiskEvaluationEntity r WHERE r.id = :id")
    Optional<RiskEvaluationEntity> findByIdWithCreditApplication(@Param("id") Long id);

    @Query("SELECT r FROM RiskEvaluationEntity r " +
           "JOIN FETCH r.creditApplication c " +
           "JOIN FETCH c.affiliate " +
           "WHERE r.creditApplication.id = :creditApplicationId")
    Optional<RiskEvaluationEntity> findByCreditApplicationIdWithDetails(
            @Param("creditApplicationId") Long creditApplicationId);
}
