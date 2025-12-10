package com.riwi.microservice.coopcredit.credit.domain.port.out;

import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;

import java.util.List;
import java.util.Optional;

/**
 * Output port for affiliate persistence operations.
 */
public interface AffiliateRepositoryPort {

    /**
     * Save an affiliate.
     * @param affiliate the affiliate to save
     * @return the saved affiliate
     */
    Affiliate save(Affiliate affiliate);

    /**
     * Find an affiliate by ID.
     * @param id the affiliate ID
     * @return the affiliate if found
     */
    Optional<Affiliate> findById(Long id);

    /**
     * Find an affiliate by document.
     * @param document the document number
     * @return the affiliate if found
     */
    Optional<Affiliate> findByDocument(String document);

    /**
     * Find an affiliate by email.
     * @param email the email address
     * @return the affiliate if found
     */
    Optional<Affiliate> findByEmail(String email);

    /**
     * Check if an affiliate exists with the given document.
     * @param document the document number
     * @return true if exists, false otherwise
     */
    boolean existsByDocument(String document);

    /**
     * Check if an affiliate exists with the given email.
     * @param email the email address
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if an affiliate exists by ID.
     * @param id the affiliate ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long id);

    /**
     * Find all affiliates with pagination.
     * @param page the page number
     * @param size the page size
     * @return list of affiliates
     */
    List<Affiliate> findAll(int page, int size);

    /**
     * Find affiliates by status with pagination.
     * @param status the affiliate status
     * @param page the page number
     * @param size the page size
     * @return list of affiliates with the given status
     */
    List<Affiliate> findByStatus(AffiliateStatus status, int page, int size);

    /**
     * Find affiliate with credit applications loaded (using EntityGraph).
     * @param id the affiliate ID
     * @return the affiliate with credit applications if found
     */
    Optional<Affiliate> findByIdWithCreditApplications(Long id);

    /**
     * Delete an affiliate.
     * @param id the affiliate ID
     */
    void deleteById(Long id);

    /**
     * Count all affiliates.
     * @return the total count
     */
    long count();

    /**
     * Count affiliates by status.
     * @param status the affiliate status
     * @return the count of affiliates with the given status
     */
    long countByStatus(AffiliateStatus status);
}
