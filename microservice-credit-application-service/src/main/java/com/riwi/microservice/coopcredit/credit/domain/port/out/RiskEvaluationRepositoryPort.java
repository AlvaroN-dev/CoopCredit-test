package com.riwi.microservice.coopcredit.credit.domain.port.out;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;

import java.util.List;
import java.util.Optional;

/**
 * Output port for risk evaluation persistence operations.
 */
public interface RiskEvaluationRepositoryPort {

    /**
     * Save a risk evaluation.
     * @param riskEvaluation the risk evaluation to save
     * @return the saved risk evaluation
     */
    RiskEvaluation save(RiskEvaluation riskEvaluation);

    /**
     * Find a risk evaluation by ID.
     * @param id the evaluation ID
     * @return the risk evaluation if found
     */
    Optional<RiskEvaluation> findById(Long id);

    /**
     * Find a risk evaluation by credit application ID.
     * @param creditApplicationId the credit application ID
     * @return the risk evaluation if found
     */
    Optional<RiskEvaluation> findByCreditApplicationId(Long creditApplicationId);

    /**
     * Find risk evaluations by risk level with pagination.
     * @param riskLevel the risk level
     * @param page the page number
     * @param size the page size
     * @return list of risk evaluations
     */
    List<RiskEvaluation> findByRiskLevel(RiskLevel riskLevel, int page, int size);

    /**
     * Find all risk evaluations with pagination.
     * @param page the page number
     * @param size the page size
     * @return list of risk evaluations
     */
    List<RiskEvaluation> findAll(int page, int size);

    /**
     * Find risk evaluation with credit application loaded (using EntityGraph).
     * @param id the evaluation ID
     * @return the risk evaluation with credit application if found
     */
    Optional<RiskEvaluation> findByIdWithCreditApplication(Long id);

    /**
     * Check if a risk evaluation exists for a credit application.
     * @param creditApplicationId the credit application ID
     * @return true if exists, false otherwise
     */
    boolean existsByCreditApplicationId(Long creditApplicationId);

    /**
     * Delete a risk evaluation.
     * @param id the evaluation ID
     */
    void deleteById(Long id);

    /**
     * Count all risk evaluations.
     * @return the total count
     */
    long count();

    /**
     * Count risk evaluations by risk level.
     * @param riskLevel the risk level
     * @return the count of evaluations with the given risk level
     */
    long countByRiskLevel(RiskLevel riskLevel);

    /**
     * Find risk evaluations by evaluated by user.
     * @param evaluatedBy the username of the evaluator
     * @param page the page number
     * @param size the page size
     * @return list of risk evaluations
     */
    List<RiskEvaluation> findByEvaluatedBy(String evaluatedBy, int page, int size);
}
