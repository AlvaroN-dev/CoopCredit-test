package com.riwi.microservice.coopcredit.risk.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request for risk evaluation")
public class RiskEvaluationRequest {
    @Schema(description = "Document number", example = "123456789")
    private String documento;
    @Schema(description = "Requested amount", example = "5000000")
    private Double monto;
    @Schema(description = "Term in months", example = "12")
    private Integer plazo;

    public RiskEvaluationRequest() {
    }

    public RiskEvaluationRequest(String documento, Double monto, Integer plazo) {
        this.documento = documento;
        this.monto = monto;
        this.plazo = plazo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }
}
