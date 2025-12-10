package com.riwi.microservice.coopcredit.credit.infrastructure.entities;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA Entity for Risk Evaluation.
 */
@Entity
@Table(name = "risk_evaluations", indexes = {
        @Index(name = "idx_risk_evaluation_credit_app", columnList = "credit_application_id", unique = true),
        @Index(name = "idx_risk_evaluation_risk_level", columnList = "risk_level"),
        @Index(name = "idx_risk_evaluation_evaluated_by", columnList = "evaluated_by")
})
@NamedEntityGraph(
        name = "RiskEvaluation.withCreditApplication",
        attributeNodes = @NamedAttributeNode("creditApplication")
)
public class RiskEvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "credit_score", nullable = false)
    private Integer creditScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel;

    @Column(name = "debt_to_income_ratio", nullable = false, precision = 5, scale = 2)
    private BigDecimal debtToIncomeRatio;

    @Column(name = "has_default_history", nullable = false)
    private Boolean hasDefaultHistory;

    @Column(name = "years_employed", nullable = false)
    private Integer yearsEmployed;

    @Column(name = "has_guarantor", nullable = false)
    private Boolean hasGuarantor;

    @Column(name = "collateral_value", precision = 15, scale = 2)
    private BigDecimal collateralValue;

    @Column(name = "evaluation_notes", length = 2000)
    private String evaluationNotes;

    @Column(name = "recommendation", length = 500)
    private String recommendation;

    @Column(name = "approved")
    private Boolean approved;

    @Column(name = "evaluated_by", nullable = false, length = 100)
    private String evaluatedBy;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDateTime evaluationDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_application_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "fk_risk_evaluation_credit_application"))
    private CreditApplicationEntity creditApplication;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (evaluationDate == null) {
            evaluationDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

    public CreditApplicationEntity getCreditApplication() { return creditApplication; }
    public void setCreditApplication(CreditApplicationEntity creditApplication) { this.creditApplication = creditApplication; }
}
