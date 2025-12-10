package com.riwi.microservice.coopcredit.risk.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request for risk evaluation")
public class RiskEvaluationRequest {
    @Schema(description = "Document number", example = "123456789")
    private String documento;
    @Schema(description = "Requested amount", example = "5000000")
    private Double monto;
    @Schema(description = "Term in months", example = "12")
    private Integer plazo;

}
