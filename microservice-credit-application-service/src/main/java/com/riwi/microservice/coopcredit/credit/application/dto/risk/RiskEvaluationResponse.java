package com.riwi.microservice.coopcredit.credit.application.dto.risk;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for Risk Evaluation.
 */
public class RiskEvaluationResponse {
    
    private Long id;
    private Integer creditScore;
    private RiskLevel riskLevel;
    private BigDecimal debtToIncomeRatio;
    private Boolean hasDefaultHistory;
    private Integer yearsEmployed;
    private Boolean hasGuarantor;
    private BigDecimal collateralValue;
    private String evaluationNotes;
    private String recommendation;
    private Boolean approved;
    private String evaluatedBy;
    private LocalDateTime evaluationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer compositeRiskScore;

    public RiskEvaluationResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public BigDecimal getDebtToIncomeRatio() { return debtToIncomeRatio; }
    public void setDebtToIncomeRatio(BigDecimal debtToIncomeRatio) { this.debtToIncomeRatio = debtToIncomeRatio; }

    public Boolean getHasDefaultHistory() { return hasDefaultHistory; }
    public void setHasDefaultHistory(Boolean hasDefaultHistory) { this.hasDefaultHistory = hasDefaultHistory; }

    public Integer getYearsEmployed() { return yearsEmployed; }
    public void setYearsEmployed(Integer yearsEmployed) { this.yearsEmployed = yearsEmployed; }

    public Boolean getHasGuarantor() { return hasGuarantor; }
    public void setHasGuarantor(Boolean hasGuarantor) { this.hasGuarantor = hasGuarantor; }

    public BigDecimal getCollateralValue() { return collateralValue; }
    public void setCollateralValue(BigDecimal collateralValue) { this.collateralValue = collateralValue; }

    public String getEvaluationNotes() { return evaluationNotes; }
    public void setEvaluationNotes(String evaluationNotes) { this.evaluationNotes = evaluationNotes; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }

    public String getEvaluatedBy() { return evaluatedBy; }
    public void setEvaluatedBy(String evaluatedBy) { this.evaluatedBy = evaluatedBy; }

    public LocalDateTime getEvaluationDate() { return evaluationDate; }
    public void setEvaluationDate(LocalDateTime evaluationDate) { this.evaluationDate = evaluationDate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Integer getCompositeRiskScore() { return compositeRiskScore; }
    public void setCompositeRiskScore(Integer compositeRiskScore) { this.compositeRiskScore = compositeRiskScore; }
}
