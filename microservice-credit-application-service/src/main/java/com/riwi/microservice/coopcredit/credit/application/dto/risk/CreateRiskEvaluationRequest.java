package com.riwi.microservice.coopcredit.credit.application.dto.risk;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request DTO for creating a Risk Evaluation.
 */
public class CreateRiskEvaluationRequest {

    @NotNull(message = "El ID de la solicitud de crédito es obligatorio")
    private Long creditApplicationId;

    @NotNull(message = "El puntaje crediticio es obligatorio")
    @Min(value = 300, message = "El puntaje crediticio mínimo es 300")
    @Max(value = 850, message = "El puntaje crediticio máximo es 850")
    private Integer creditScore;

    @NotNull(message = "El ratio deuda/ingreso es obligatorio")
    @PositiveOrZero(message = "El ratio deuda/ingreso no puede ser negativo")
    @DecimalMax(value = "100", message = "El ratio deuda/ingreso no puede exceder 100%")
    private BigDecimal debtToIncomeRatio;

    @NotNull(message = "Debe indicar si tiene historial de incumplimiento")
    private Boolean hasDefaultHistory;

    @NotNull(message = "Los años de empleo son obligatorios")
    @PositiveOrZero(message = "Los años de empleo no pueden ser negativos")
    private Integer yearsEmployed;

    @NotNull(message = "Debe indicar si tiene codeudor")
    private Boolean hasGuarantor;

    @PositiveOrZero(message = "El valor de la garantía no puede ser negativo")
    private BigDecimal collateralValue;

    @Size(max = 2000, message = "Las notas de evaluación no pueden exceder 2000 caracteres")
    private String evaluationNotes;

    @NotBlank(message = "El evaluador es obligatorio")
    @Size(max = 100, message = "El nombre del evaluador no puede exceder 100 caracteres")
    private String evaluatedBy;

    // Getters and Setters
    public Long getCreditApplicationId() { return creditApplicationId; }
    public void setCreditApplicationId(Long creditApplicationId) { this.creditApplicationId = creditApplicationId; }

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

    public String getEvaluatedBy() { return evaluatedBy; }
    public void setEvaluatedBy(String evaluatedBy) { this.evaluatedBy = evaluatedBy; }
}
