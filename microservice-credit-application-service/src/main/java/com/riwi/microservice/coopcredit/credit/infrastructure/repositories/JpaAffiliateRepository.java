package com.riwi.microservice.coopcredit.credit.infrastructure.repositories;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.AffiliateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for AffiliateEntity.
 */
@Repository
public interface JpaAffiliateRepository extends JpaRepository<AffiliateEntity, Long> {

    Optional<AffiliateEntity> findByDocument(String document);

    Optional<AffiliateEntity> findByEmail(String email);

    boolean existsByDocument(String document);

    boolean existsByEmail(String email);

    Page<AffiliateEntity> findByStatus(AffiliateStatus status, Pageable pageable);

    long countByStatus(AffiliateStatus status);

    @EntityGraph(value = "Affiliate.withCreditApplications", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT a FROM AffiliateEntity a WHERE a.id = :id")
    Optional<AffiliateEntity> findByIdWithCreditApplications(@Param("id") Long id);

    @Query("SELECT a FROM AffiliateEntity a LEFT JOIN FETCH a.creditApplications WHERE a.document = :document")
    Optional<AffiliateEntity> findByDocumentWithCreditApplications(@Param("document") String document);
}
