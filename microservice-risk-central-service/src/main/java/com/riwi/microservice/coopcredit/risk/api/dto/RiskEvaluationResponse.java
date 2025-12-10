package com.riwi.microservice.coopcredit.risk.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for risk evaluation")
public class RiskEvaluationResponse {
    @Schema(description = "Document number", example = "123456789")
    private String documento;
    @Schema(description = "Risk score (300-950)", example = "750")
    private Integer score;
    @Schema(description = "Risk level", example = "BAJO RIESGO")
    private String nivelRiesgo;
    @Schema(description = "Detailed explanation", example = "Excelente comportamiento crediticio.")
    private String detalle;

}
