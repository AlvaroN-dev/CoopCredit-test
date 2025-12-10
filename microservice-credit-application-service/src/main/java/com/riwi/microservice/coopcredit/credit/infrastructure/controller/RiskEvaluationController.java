package com.riwi.microservice.coopcredit.credit.infrastructure.controller;

import com.riwi.microservice.coopcredit.credit.application.dto.risk.CreateRiskEvaluationRequest;
import com.riwi.microservice.coopcredit.credit.application.dto.risk.RiskEvaluationResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.risk.UpdateRiskEvaluationRequest;
import com.riwi.microservice.coopcredit.credit.application.mapper.RiskEvaluationMapper;
import com.riwi.microservice.coopcredit.credit.domain.exception.RiskEvaluationNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CalculateRiskLevelUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateRiskEvaluationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.RetrieveRiskEvaluationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.UpdateRiskEvaluationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Risk Evaluation operations.
 */
@RestController
@RequestMapping("/api/risk-evaluations")
@Tag(name = "Risk Evaluations", description = "Operations related to risk evaluations")
public class RiskEvaluationController {

    private final CreateRiskEvaluationUseCase createRiskEvaluationUseCase;
    private final RetrieveRiskEvaluationUseCase retrieveRiskEvaluationUseCase;
    private final UpdateRiskEvaluationUseCase updateRiskEvaluationUseCase;
    private final CalculateRiskLevelUseCase calculateRiskLevelUseCase;
    private final RiskEvaluationMapper riskEvaluationMapper;

    public RiskEvaluationController(
            CreateRiskEvaluationUseCase createRiskEvaluationUseCase,
            RetrieveRiskEvaluationUseCase retrieveRiskEvaluationUseCase,
            UpdateRiskEvaluationUseCase updateRiskEvaluationUseCase,
            CalculateRiskLevelUseCase calculateRiskLevelUseCase,
            RiskEvaluationMapper riskEvaluationMapper) {
        this.createRiskEvaluationUseCase = createRiskEvaluationUseCase;
        this.retrieveRiskEvaluationUseCase = retrieveRiskEvaluationUseCase;
        this.updateRiskEvaluationUseCase = updateRiskEvaluationUseCase;
        this.calculateRiskLevelUseCase = calculateRiskLevelUseCase;
        this.riskEvaluationMapper = riskEvaluationMapper;
    }

    /**
     * Create a new risk evaluation.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Create risk evaluation", description = "Creates a new risk evaluation")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Evaluación de riesgo creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RiskEvaluationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de evaluación inválidos",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<RiskEvaluationResponse> createRiskEvaluation(
            @Valid @RequestBody CreateRiskEvaluationRequest request) {
        RiskEvaluation riskEvaluation = createRiskEvaluationUseCase.createRiskEvaluation(
                riskEvaluationMapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(riskEvaluationMapper.toResponse(riskEvaluation));
    }

    /**
     * Get risk evaluation by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Get risk evaluation by ID", description = "Retrieves a risk evaluation by its unique ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Evaluación de riesgo recuperada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RiskEvaluationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Evaluación de riesgo no encontrada",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<RiskEvaluationResponse> getRiskEvaluationById(@PathVariable Long id) {
        RiskEvaluation riskEvaluation = retrieveRiskEvaluationUseCase.getRiskEvaluationById(id)
                .orElseThrow(() -> new RiskEvaluationNotFoundException(id));
        return ResponseEntity.ok(riskEvaluationMapper.toResponse(riskEvaluation));
    }

    /**
     * Get risk evaluation by credit application ID.
     */
    @GetMapping("/credit-application/{creditApplicationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Get risk evaluation by credit application", description = "Retrieves the risk evaluation associated with a credit application")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Evaluación de riesgo recuperada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RiskEvaluationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Evaluación de riesgo no encontrada para la solicitud de crédito dada",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<RiskEvaluationResponse> getRiskEvaluationByCreditApplicationId(
            @PathVariable Long creditApplicationId) {
        RiskEvaluation riskEvaluation = retrieveRiskEvaluationUseCase.getRiskEvaluationByCreditApplicationId(creditApplicationId)
                .orElseThrow(() -> new RiskEvaluationNotFoundException(creditApplicationId, true));
        return ResponseEntity.ok(riskEvaluationMapper.toResponse(riskEvaluation));
    }

    /**
     * Get all risk evaluations by risk level.
     */
    @GetMapping("/risk-level/{riskLevel}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Get risk evaluations by level", description = "Retrieves a list of risk evaluations filtered by risk level")
    public ResponseEntity<List<RiskEvaluationResponse>> getRiskEvaluationsByRiskLevel(
            @PathVariable RiskLevel riskLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<RiskEvaluation> evaluations = retrieveRiskEvaluationUseCase.getRiskEvaluationsByRiskLevel(riskLevel, page, size);
        List<RiskEvaluationResponse> responses = evaluations.stream()
                .map(riskEvaluationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all risk evaluations with pagination.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    public ResponseEntity<List<RiskEvaluationResponse>> getAllRiskEvaluations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<RiskEvaluation> evaluations = retrieveRiskEvaluationUseCase.getAllRiskEvaluations(page, size);
        List<RiskEvaluationResponse> responses = evaluations.stream()
                .map(riskEvaluationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Update a risk evaluation.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    public ResponseEntity<RiskEvaluationResponse> updateRiskEvaluation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRiskEvaluationRequest request) {
        RiskEvaluation riskEvaluation = updateRiskEvaluationUseCase.updateRiskEvaluation(
                id, riskEvaluationMapper.toUpdateCommand(request));
        return ResponseEntity.ok(riskEvaluationMapper.toResponse(riskEvaluation));
    }

    /**
     * Recalculate risk level for an evaluation.
     */
    @PostMapping("/{id}/calculate-risk")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    public ResponseEntity<RiskEvaluationResponse> calculateRiskLevel(@PathVariable Long id) {
        RiskEvaluation riskEvaluation = calculateRiskLevelUseCase.calculateRiskLevel(id);
        return ResponseEntity.ok(riskEvaluationMapper.toResponse(riskEvaluation));
    }
}
