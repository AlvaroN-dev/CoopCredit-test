package com.riwi.microservice.coopcredit.risk.api.controller;

import com.riwi.microservice.coopcredit.risk.api.dto.RiskEvaluationRequest;
import com.riwi.microservice.coopcredit.risk.api.dto.RiskEvaluationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/risk/evaluation")
@Tag(name = "Risk Evaluation Central", description = "Centralized risk evaluation service")
public class RiskEvaluationController {

    @PostMapping
    @Operation(summary = "Evaluate risk", description = "Evaluates the risk based on the document number")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Evaluación de riesgo completada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RiskEvaluationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Documento inválido o faltante",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<RiskEvaluationResponse> evaluateRisk(@RequestBody RiskEvaluationRequest request) {
        if (request.getDocumento() == null) {
            return ResponseEntity.badRequest().build();
        }

        // 1. Convertir el documento en un seed numérico (hash mod 1000).
        int seed = Math.abs(request.getDocumento().hashCode()) % 1000;

        // 2. Generar un score entre 300 y 950 basado en ese seed.
        // Range size = 950 - 300 = 650.
        // Scaled value = seed * 650 / 999.
        int score = 300 + (seed * 650 / 999);

        // 3. Clasificar
        String nivelRiesgo;
        String detalle;

        if (score <= 500) {
            nivelRiesgo = "ALTO RIESGO";
            detalle = "Historial crediticio deficiente o insuficiente.";
        } else if (score <= 700) {
            nivelRiesgo = "MEDIO RIESGO";
            detalle = "Historial crediticio moderado.";
        } else {
            nivelRiesgo = "BAJO RIESGO";
            detalle = "Excelente comportamiento crediticio.";
        }

        RiskEvaluationResponse response = new RiskEvaluationResponse(
                request.getDocumento(),
                score,
                nivelRiesgo,
                detalle
        );

        return ResponseEntity.ok(response);
    }
}
