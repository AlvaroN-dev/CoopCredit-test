package com.riwi.microservice.coopcredit.credit.infrastructure.repositories;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.CreditApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for CreditApplicationEntity.
 */
@Repository
public interface JpaCreditApplicationRepository extends JpaRepository<CreditApplicationEntity, Long> {

    Optional<CreditApplicationEntity> findByApplicationNumber(String applicationNumber);

    boolean existsByApplicationNumber(String applicationNumber);

    List<CreditApplicationEntity> findByAffiliateId(Long affiliateId);

    Page<CreditApplicationEntity> findByStatus(CreditApplicationStatus status, Pageable pageable);

    Page<CreditApplicationEntity> findByApplicationDateBetween(
            LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    long countByStatus(CreditApplicationStatus status);

    long countByAffiliateId(Long affiliateId);

    @EntityGraph(value = "CreditApplication.withDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT c FROM CreditApplicationEntity c WHERE c.id = :id")
    Optional<CreditApplicationEntity> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT c FROM CreditApplicationEntity c " +
           "LEFT JOIN FETCH c.affiliate " +
           "LEFT JOIN FETCH c.riskEvaluation " +
           "WHERE c.applicationNumber = :applicationNumber")
    Optional<CreditApplicationEntity> findByApplicationNumberWithDetails(
            @Param("applicationNumber") String applicationNumber);

    @Query("SELECT c FROM CreditApplicationEntity c " +
           "LEFT JOIN FETCH c.riskEvaluation " +
           "WHERE c.affiliate.id = :affiliateId")
    List<CreditApplicationEntity> findByAffiliateIdWithRiskEvaluations(@Param("affiliateId") Long affiliateId);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(c.applicationNumber, 5) AS long)), 0) FROM CreditApplicationEntity c " +
           "WHERE c.applicationNumber LIKE CONCAT(:prefix, '%')")
    Long findMaxApplicationNumberSequence(@Param("prefix") String prefix);
}
