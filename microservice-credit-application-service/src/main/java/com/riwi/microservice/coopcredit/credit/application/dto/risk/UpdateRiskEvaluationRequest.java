package com.riwi.microservice.coopcredit.credit.application.dto.risk;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Request DTO for updating a Risk Evaluation.
 */

@Getter
@Setter
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

}
