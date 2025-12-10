package com.riwi.microservice.coopcredit.credit.domain.port.out;

import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Output port for credit application persistence operations.
 */
public interface CreditApplicationRepositoryPort {

    /**
     * Save a credit application.
     * @param creditApplication the credit application to save
     * @return the saved credit application
     */
    CreditApplication save(CreditApplication creditApplication);

    /**
     * Find a credit application by ID.
     * @param id the application ID
     * @return the credit application if found
     */
    Optional<CreditApplication> findById(Long id);

    /**
     * Find a credit application by application number.
     * @param applicationNumber the application number
     * @return the credit application if found
     */
    Optional<CreditApplication> findByApplicationNumber(String applicationNumber);

    /**
     * Find all credit applications by affiliate ID.
     * @param affiliateId the affiliate ID
     * @return list of credit applications
     */
    List<CreditApplication> findByAffiliateId(Long affiliateId);

    /**
     * Find credit applications by status with pagination.
     * @param status the application status
     * @param page the page number
     * @param size the page size
     * @return list of credit applications
     */
    List<CreditApplication> findByStatus(CreditApplicationStatus status, int page, int size);

    /**
     * Find all credit applications with pagination.
     * @param page the page number
     * @param size the page size
     * @return list of credit applications
     */
    List<CreditApplication> findAll(int page, int size);

    /**
     * Find credit application with affiliate and risk evaluation loaded (using EntityGraph).
     * @param id the application ID
     * @return the credit application with all related entities if found
     */
    Optional<CreditApplication> findByIdWithDetails(Long id);

    /**
     * Find credit applications by affiliate ID with risk evaluations loaded.
     * @param affiliateId the affiliate ID
     * @return list of credit applications with risk evaluations
     */
    List<CreditApplication> findByAffiliateIdWithRiskEvaluations(Long affiliateId);

    /**
     * Find credit applications created within a date range.
     * @param startDate the start date
     * @param endDate the end date
     * @param page the page number
     * @param size the page size
     * @return list of credit applications
     */
    List<CreditApplication> findByApplicationDateBetween(
            LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    /**
     * Check if an application number already exists.
     * @param applicationNumber the application number
     * @return true if exists, false otherwise
     */
    boolean existsByApplicationNumber(String applicationNumber);

    /**
     * Delete a credit application.
     * @param id the application ID
     */
    void deleteById(Long id);

    /**
     * Count all credit applications.
     * @return the total count
     */
    long count();

    /**
     * Count credit applications by status.
     * @param status the application status
     * @return the count of applications with the given status
     */
    long countByStatus(CreditApplicationStatus status);

    /**
     * Count credit applications by affiliate.
     * @param affiliateId the affiliate ID
     * @return the count of applications for the affiliate
     */
    long countByAffiliateId(Long affiliateId);

    /**
     * Generate a unique application number.
     * @return the generated application number
     */
    String generateApplicationNumber();
}
