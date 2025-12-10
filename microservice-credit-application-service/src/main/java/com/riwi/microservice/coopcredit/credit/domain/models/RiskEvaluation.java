package com.riwi.microservice.coopcredit.credit.domain.models;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model representing a Risk Evaluation.
 * Contains business rules for risk assessment.
 */
public class RiskEvaluation {
    
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
    private CreditApplication creditApplication;

    public RiskEvaluation() {
        this.evaluationDate = LocalDateTime.now();
    }

    public RiskEvaluation(Long id, Integer creditScore, RiskLevel riskLevel,
                          BigDecimal debtToIncomeRatio, Boolean hasDefaultHistory) {
        this();
        this.id = id;
        this.creditScore = creditScore;
        this.riskLevel = riskLevel;
        this.debtToIncomeRatio = debtToIncomeRatio;
        this.hasDefaultHistory = hasDefaultHistory;
    }

    // Business Rules

    /**
     * Calculates the risk level based on credit score.
     */
    public RiskLevel calculateRiskLevelFromScore() {
        if (this.creditScore == null) {
            return RiskLevel.MUY_ALTO;
        }
        
        if (this.creditScore >= 750) {
            return RiskLevel.BAJO;
        } else if (this.creditScore >= 650) {
            return RiskLevel.MEDIO;
        } else if (this.creditScore >= 550) {
            return RiskLevel.ALTO;
        } else {
            return RiskLevel.MUY_ALTO;
        }
    }

    /**
     * Determines if the application should be recommended for approval.
     * Business Rules:
     * - Credit score >= 600
     * - DTI <= 40%
     * - No default history (or has guarantor/collateral)
     */
    public boolean shouldRecommendApproval() {
        // Minimum credit score
        if (this.creditScore == null || this.creditScore < 600) {
            return false;
        }
        
        // DTI check
        if (this.debtToIncomeRatio != null && 
            this.debtToIncomeRatio.compareTo(BigDecimal.valueOf(40)) > 0) {
            return false;
        }
        
        // Default history check with mitigation
        if (Boolean.TRUE.equals(this.hasDefaultHistory)) {
            // Can be approved if has guarantor or collateral
            return Boolean.TRUE.equals(this.hasGuarantor) || 
                   (this.collateralValue != null && this.collateralValue.compareTo(BigDecimal.ZERO) > 0);
        }
        
        return true;
    }

    /**
     * Calculates a composite risk score (0-100, higher is riskier).
     */
    public int calculateCompositeRiskScore() {
        int score = 0;
        
        // Credit score contribution (0-40 points)
        if (this.creditScore != null) {
            if (this.creditScore < 550) {
                score += 40;
            } else if (this.creditScore < 650) {
                score += 30;
            } else if (this.creditScore < 750) {
                score += 15;
            }
        } else {
            score += 40;
        }
        
        // DTI contribution (0-30 points)
        if (this.debtToIncomeRatio != null) {
            if (this.debtToIncomeRatio.compareTo(BigDecimal.valueOf(50)) > 0) {
                score += 30;
            } else if (this.debtToIncomeRatio.compareTo(BigDecimal.valueOf(40)) > 0) {
                score += 20;
            } else if (this.debtToIncomeRatio.compareTo(BigDecimal.valueOf(30)) > 0) {
                score += 10;
            }
        }
        
        // Default history contribution (0-20 points)
        if (Boolean.TRUE.equals(this.hasDefaultHistory)) {
            score += 20;
        }
        
        // Employment stability contribution (0-10 points)
        if (this.yearsEmployed != null) {
            if (this.yearsEmployed < 1) {
                score += 10;
            } else if (this.yearsEmployed < 2) {
                score += 5;
            }
        } else {
            score += 10;
        }
        
        // Mitigation factors
        if (Boolean.TRUE.equals(this.hasGuarantor)) {
            score -= 10;
        }
        if (this.collateralValue != null && this.collateralValue.compareTo(BigDecimal.ZERO) > 0) {
            score -= 10;
        }
        
        return Math.max(0, Math.min(100, score));
    }

    /**
     * Generates automatic recommendation based on evaluation.
     */
    public String generateRecommendation() {
        StringBuilder recommendation = new StringBuilder();
        int riskScore = calculateCompositeRiskScore();
        
        if (riskScore <= 25) {
            recommendation.append("APROBAR - Perfil de riesgo bajo. ");
        } else if (riskScore <= 50) {
            recommendation.append("APROBAR CON CONDICIONES - Perfil de riesgo moderado. ");
        } else if (riskScore <= 75) {
            recommendation.append("REVISAR - Perfil de riesgo alto. Considerar garantías adicionales. ");
        } else {
            recommendation.append("RECHAZAR - Perfil de riesgo muy alto. ");
        }
        
        // Add specific observations
        if (this.creditScore != null && this.creditScore < 600) {
            recommendation.append("Score crediticio bajo. ");
        }
        if (this.debtToIncomeRatio != null && this.debtToIncomeRatio.compareTo(BigDecimal.valueOf(40)) > 0) {
            recommendation.append("DTI elevado. ");
        }
        if (Boolean.TRUE.equals(this.hasDefaultHistory)) {
            recommendation.append("Historial de incumplimiento. ");
        }
        
        return recommendation.toString().trim();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public BigDecimal getDebtToIncomeRatio() {
        return debtToIncomeRatio;
    }

    public void setDebtToIncomeRatio(BigDecimal debtToIncomeRatio) {
        this.debtToIncomeRatio = debtToIncomeRatio;
    }

    public Boolean getHasDefaultHistory() {
        return hasDefaultHistory;
    }

    public void setHasDefaultHistory(Boolean hasDefaultHistory) {
        this.hasDefaultHistory = hasDefaultHistory;
    }

    public Integer getYearsEmployed() {
        return yearsEmployed;
    }

    public void setYearsEmployed(Integer yearsEmployed) {
        this.yearsEmployed = yearsEmployed;
    }

    public Boolean getHasGuarantor() {
        return hasGuarantor;
    }

    public void setHasGuarantor(Boolean hasGuarantor) {
        this.hasGuarantor = hasGuarantor;
    }

    public BigDecimal getCollateralValue() {
        return collateralValue;
    }

    public void setCollateralValue(BigDecimal collateralValue) {
        this.collateralValue = collateralValue;
    }

    public String getEvaluationNotes() {
        return evaluationNotes;
    }

    public void setEvaluationNotes(String evaluationNotes) {
        this.evaluationNotes = evaluationNotes;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getEvaluatedBy() {
        return evaluatedBy;
    }

    public void setEvaluatedBy(String evaluatedBy) {
        this.evaluatedBy = evaluatedBy;
    }

    public LocalDateTime getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDateTime evaluationDate) {
        this.evaluationDate = evaluationDate;
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

    public CreditApplication getCreditApplication() {
        return creditApplication;
    }

    public void setCreditApplication(CreditApplication creditApplication) {
        this.creditApplication = creditApplication;
    }
}
