package com.riwi.microservice.coopcredit.credit.application.dto.credit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Request DTO for creating a Credit Application.
 */
@Getter
@Setter
@Schema(description = "Request DTO for creating a Credit Application")
public class CreateCreditApplicationRequest {

    @NotNull(message = "El ID del afiliado es obligatorio")
    @Schema(description = "ID of the affiliate", example = "1")
    private Long affiliateId;

    @NotNull(message = "El monto solicitado es obligatorio")
    @Positive(message = "El monto solicitado debe ser mayor a cero")
    @DecimalMin(value = "100000", message = "El monto mínimo es de $100,000")
    @DecimalMax(value = "500000000", message = "El monto máximo es de $500,000,000")
    @Schema(description = "Requested amount", example = "5000000")
    private BigDecimal requestedAmount;

    @NotNull(message = "El plazo en meses es obligatorio")
    @Min(value = 6, message = "El plazo mínimo es de 6 meses")
    @Max(value = 84, message = "El plazo máximo es de 84 meses")
    @Schema(description = "Term in months", example = "12")
    private Integer termMonths;

    @NotNull(message = "La tasa de interés es obligatoria")
    @Positive(message = "La tasa de interés debe ser mayor a cero")
    @DecimalMax(value = "100", message = "La tasa de interés no puede exceder 100%")
    @Schema(description = "Interest rate", example = "1.5")
    private BigDecimal interestRate;

    @NotBlank(message = "El propósito del crédito es obligatorio")
    @Size(min = 10, max = 500, message = "El propósito debe tener entre 10 y 500 caracteres")
    @Schema(description = "Purpose of the credit", example = "Home improvement")
    private String purpose;

}
