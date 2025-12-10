package com.riwi.microservice.coopcredit.credit.domain.models;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Domain model representing a Credit Application.
 * Contains business rules and invariants.
 */
public class CreditApplication {
    
    private Long id;
    private String applicationNumber;
    private BigDecimal requestedAmount;
    private Integer termMonths;
    private BigDecimal interestRate;
    private String purpose;
    private CreditApplicationStatus status;
    private String comments;
    private LocalDateTime applicationDate;
    private LocalDateTime reviewDate;
    private LocalDateTime decisionDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Affiliate affiliate;
    private RiskEvaluation riskEvaluation;

    public CreditApplication() {
        this.applicationDate = LocalDateTime.now();
        this.status = CreditApplicationStatus.PENDIENTE;
    }

    public CreditApplication(Long id, String applicationNumber, BigDecimal requestedAmount,
                             Integer termMonths, BigDecimal interestRate, String purpose) {
        this();
        this.id = id;
        this.applicationNumber = applicationNumber;
        this.requestedAmount = requestedAmount;
        this.termMonths = termMonths;
        this.interestRate = interestRate;
        this.purpose = purpose;
    }

    // Business Rules

    /**
     * Validates that the requested amount is within affiliate's maximum credit limit.
     */
    public boolean isAmountWithinLimit() {
        if (this.affiliate == null) {
            return false;
        }
        BigDecimal maxAmount = this.affiliate.calculateMaxCreditAmount();
        return this.requestedAmount.compareTo(maxAmount) <= 0;
    }

    /**
     * Validates that the requested amount is positive.
     */
    public boolean hasValidAmount() {
        return this.requestedAmount != null && this.requestedAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Validates that the term is within acceptable range (6-84 months).
     */
    public boolean hasValidTerm() {
        return this.termMonths != null && this.termMonths >= 6 && this.termMonths <= 84;
    }

    /**
     * Calculates the monthly payment using simple interest formula.
     */
    public BigDecimal calculateMonthlyPayment() {
        if (this.requestedAmount == null || this.termMonths == null || this.interestRate == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal monthlyRate = this.interestRate.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP)
                                                   .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        
        // PMT = P * [r(1+r)^n] / [(1+r)^n - 1]
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(this.termMonths);
        
        BigDecimal numerator = this.requestedAmount.multiply(monthlyRate).multiply(onePlusRPowN);
        BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);
        
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return this.requestedAmount.divide(BigDecimal.valueOf(this.termMonths), 2, RoundingMode.HALF_UP);
        }
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the debt-to-income ratio.
     */
    public BigDecimal calculateDebtToIncomeRatio() {
        if (this.affiliate == null || this.affiliate.getSalary() == null || 
            this.affiliate.getSalary().compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal monthlyPayment = calculateMonthlyPayment();
        return monthlyPayment.divide(this.affiliate.getSalary(), 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Checks if the application can be approved based on debt-to-income ratio.
     * Business Rule: DTI should not exceed 40%
     */
    public boolean meetsDebtToIncomeRequirement() {
        BigDecimal dti = calculateDebtToIncomeRatio();
        return dti.compareTo(BigDecimal.valueOf(40)) <= 0;
    }

    /**
     * Moves the application to review status.
     */
    public void startReview() {
        if (this.status == CreditApplicationStatus.PENDIENTE) {
            this.status = CreditApplicationStatus.EN_REVISION;
            this.reviewDate = LocalDateTime.now();
        }
    }

    /**
     * Approves the credit application.
     */
    public void approve(String comments) {
        if (this.status == CreditApplicationStatus.EN_REVISION) {
            this.status = CreditApplicationStatus.APROBADA;
            this.comments = comments;
            this.decisionDate = LocalDateTime.now();
        }
    }

    /**
     * Rejects the credit application.
     */
    public void reject(String comments) {
        if (this.status == CreditApplicationStatus.EN_REVISION) {
            this.status = CreditApplicationStatus.RECHAZADA;
            this.comments = comments;
            this.decisionDate = LocalDateTime.now();
        }
    }

    /**
     * Cancels the credit application.
     */
    public void cancel(String comments) {
        if (this.status == CreditApplicationStatus.PENDIENTE || 
            this.status == CreditApplicationStatus.EN_REVISION) {
            this.status = CreditApplicationStatus.CANCELADA;
            this.comments = comments;
            this.decisionDate = LocalDateTime.now();
        }
    }

    /**
     * Sets the risk evaluation for this application.
     */
    public void setRiskEvaluation(RiskEvaluation riskEvaluation) {
        this.riskEvaluation = riskEvaluation;
        if (riskEvaluation != null) {
            riskEvaluation.setCreditApplication(this);
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public CreditApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(CreditApplicationStatus status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }

    public LocalDateTime getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(LocalDateTime decisionDate) {
        this.decisionDate = decisionDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Affiliate getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
    }

    public RiskEvaluation getRiskEvaluation() {
        return riskEvaluation;
    }
}
