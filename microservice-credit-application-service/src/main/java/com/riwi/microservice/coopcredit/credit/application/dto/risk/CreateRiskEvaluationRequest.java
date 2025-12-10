package com.riwi.microservice.coopcredit.credit.application.dto.risk;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Request DTO for creating a Risk Evaluation.
 */

@Getter
@Setter
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

}
