package com.riwi.microservice.coopcredit.credit.application.dto.risk;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request DTO for updating a Risk Evaluation.
 */
public class UpdateRiskEvaluationRequest {

    @Min(value = 300, message = "El puntaje crediticio mínimo es 300")
    @Max(value = 850, message = "El puntaje crediticio máximo es 850")
    private Integer creditScore;

    @PositiveOrZero(message = "El ratio deuda/ingreso no puede ser negativo")
    @DecimalMax(value = "100", message = "El ratio deuda/ingreso no puede exceder 100%")
    private BigDecimal debtToIncomeRatio;

    private Boolean hasDefaultHistory;

    @PositiveOrZero(message = "Los años de empleo no pueden ser negativos")
    private Integer yearsEmployed;

    private Boolean hasGuarantor;

    @PositiveOrZero(message = "El valor de la garantía no puede ser negativo")
    private BigDecimal collateralValue;

    @Size(max = 2000, message = "Las notas de evaluación no pueden exceder 2000 caracteres")
    private String evaluationNotes;

    private Boolean approved;

    // Getters and Setters
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

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

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }
}
