package com.riwi.microservice.coopcredit.credit.application.dto.credit;

import com.riwi.microservice.coopcredit.credit.application.dto.affiliate.AffiliateResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.risk.RiskEvaluationResponse;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for Credit Application.
 */
@Schema(description = "Response DTO for Credit Application")
public class CreditApplicationResponse {
    
    @Schema(description = "Unique ID of the application", example = "1")
    private Long id;
    @Schema(description = "Application number", example = "APP-2023-001")
    private String applicationNumber;
    @Schema(description = "Requested amount", example = "5000000")
    private BigDecimal requestedAmount;
    @Schema(description = "Term in months", example = "12")
    private Integer termMonths;
    @Schema(description = "Interest rate", example = "1.5")
    private BigDecimal interestRate;
    @Schema(description = "Monthly payment amount", example = "450000")
    private BigDecimal monthlyPayment;
    @Schema(description = "Purpose of the credit", example = "Home improvement")
    private String purpose;
    @Schema(description = "Status of the application", example = "APPROVED")
    private CreditApplicationStatus status;
    @Schema(description = "Comments or notes", example = "Approved after review")
    private String comments;
    @Schema(description = "Date of application")
    private LocalDateTime applicationDate;
    @Schema(description = "Date of review")
    private LocalDateTime reviewDate;
    @Schema(description = "Date of decision")
    private LocalDateTime decisionDate;
    @Schema(description = "Record creation timestamp")
    private LocalDateTime createdAt;
    @Schema(description = "Record update timestamp")
    private LocalDateTime updatedAt;
    @Schema(description = "Affiliate details")
    private AffiliateResponse affiliate;
    @Schema(description = "Risk evaluation details")
    private RiskEvaluationResponse riskEvaluation;

    public CreditApplicationResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getApplicationNumber() { return applicationNumber; }
    public void setApplicationNumber(String applicationNumber) { this.applicationNumber = applicationNumber; }

    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }

    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public BigDecimal getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(BigDecimal monthlyPayment) { this.monthlyPayment = monthlyPayment; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public CreditApplicationStatus getStatus() { return status; }
    public void setStatus(CreditApplicationStatus status) { this.status = status; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDateTime applicationDate) { this.applicationDate = applicationDate; }

    public LocalDateTime getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDateTime reviewDate) { this.reviewDate = reviewDate; }

    public LocalDateTime getDecisionDate() { return decisionDate; }
    public void setDecisionDate(LocalDateTime decisionDate) { this.decisionDate = decisionDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public AffiliateResponse getAffiliate() { return affiliate; }
    public void setAffiliate(AffiliateResponse affiliate) { this.affiliate = affiliate; }

    public RiskEvaluationResponse getRiskEvaluation() { return riskEvaluation; }
    public void setRiskEvaluation(RiskEvaluationResponse riskEvaluation) { this.riskEvaluation = riskEvaluation; }
}
