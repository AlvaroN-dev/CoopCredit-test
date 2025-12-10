package com.riwi.microservice.coopcredit.risk.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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

    public RiskEvaluationResponse() {
    }

    public RiskEvaluationResponse(String documento, Integer score, String nivelRiesgo, String detalle) {
        this.documento = documento;
        this.score = score;
        this.nivelRiesgo = nivelRiesgo;
        this.detalle = detalle;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public void setNivelRiesgo(String nivelRiesgo) {
        this.nivelRiesgo = nivelRiesgo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
