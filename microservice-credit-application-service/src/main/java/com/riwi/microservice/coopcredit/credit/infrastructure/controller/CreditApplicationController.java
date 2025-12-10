package com.riwi.microservice.coopcredit.credit.infrastructure.controller;

import com.riwi.microservice.coopcredit.credit.application.dto.credit.CreditApplicationResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.credit.CreateCreditApplicationRequest;
import com.riwi.microservice.coopcredit.credit.application.dto.credit.CreditDecisionRequest;
import com.riwi.microservice.coopcredit.credit.application.mapper.CreditApplicationMapper;
import com.riwi.microservice.coopcredit.credit.domain.exception.CreditApplicationNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateCreditApplicationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.ProcessCreditDecisionUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.RetrieveCreditApplicationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Credit Application operations.
 */
@RestController
@RequestMapping("/credit/applications")
@Tag(name = "💳 Solicitudes de Crédito", description = "Gestión de solicitudes de crédito cooperativo")
@SecurityRequirement(name = "bearerAuth")
public class CreditApplicationController {

    private final CreateCreditApplicationUseCase createCreditApplicationUseCase;
    private final RetrieveCreditApplicationUseCase retrieveCreditApplicationUseCase;
    private final ProcessCreditDecisionUseCase processCreditDecisionUseCase;
    private final CreditApplicationMapper creditApplicationMapper;

    public CreditApplicationController(CreateCreditApplicationUseCase createCreditApplicationUseCase,
                                       RetrieveCreditApplicationUseCase retrieveCreditApplicationUseCase,
                                       ProcessCreditDecisionUseCase processCreditDecisionUseCase,
                                       CreditApplicationMapper creditApplicationMapper) {
        this.createCreditApplicationUseCase = createCreditApplicationUseCase;
        this.retrieveCreditApplicationUseCase = retrieveCreditApplicationUseCase;
        this.processCreditDecisionUseCase = processCreditDecisionUseCase;
        this.creditApplicationMapper = creditApplicationMapper;
    }

    /**
     * Create a new credit application.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA') or hasRole('AFILIADO')")
    @Operation(summary = "Create a new credit application", description = "Registers a new credit application for an affiliate")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Solicitud de crédito creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreditApplicationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de solicitud inválidos",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<CreditApplicationResponse> createCreditApplication(
            @Valid @RequestBody CreateCreditApplicationRequest request) {
        CreditApplication creditApplication = createCreditApplicationUseCase.createCreditApplication(
                creditApplicationMapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(creditApplicationMapper.toResponse(creditApplication));
    }

    /**
     * Get credit application by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA') or hasRole('AFILIADO')")
    @Operation(summary = "Get credit application by ID", description = "Retrieves a credit application by its unique ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Solicitud encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreditApplicationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Solicitud no encontrada",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<CreditApplicationResponse> getCreditApplicationById(@PathVariable Long id) {
        CreditApplication creditApplication = retrieveCreditApplicationUseCase.getCreditApplicationById(id)
                .orElseThrow(() -> new CreditApplicationNotFoundException(id));
        return ResponseEntity.ok(creditApplicationMapper.toResponse(creditApplication));
    }

    /**
     * Get credit application by application number.
     */
    @GetMapping("/number/{applicationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Get credit application by number", description = "Retrieves a credit application by its application number")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Solicitud encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreditApplicationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Solicitud no encontrada",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<CreditApplicationResponse> getCreditApplicationByNumber(
            @PathVariable String applicationNumber) {
        CreditApplication creditApplication = retrieveCreditApplicationUseCase.getCreditApplicationByNumber(applicationNumber)
                .orElseThrow(() -> new CreditApplicationNotFoundException(applicationNumber));
        return ResponseEntity.ok(creditApplicationMapper.toResponse(creditApplication));
    }

    /**
     * Get credit application with full details.
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Get credit application details", description = "Retrieves full details of a credit application")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Solicitud encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CreditApplicationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Solicitud no encontrada",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<CreditApplicationResponse> getCreditApplicationWithDetails(@PathVariable Long id) {
        CreditApplication creditApplication = retrieveCreditApplicationUseCase.getCreditApplicationWithDetails(id)
                .orElseThrow(() -> new CreditApplicationNotFoundException(id));
        return ResponseEntity.ok(creditApplicationMapper.toResponse(creditApplication));
    }

    /**
     * Get credit applications by affiliate ID.
     */
    @GetMapping("/affiliate/{affiliateId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA') or hasRole('AFILIADO')")
    @Operation(summary = "Get credit applications by affiliate", description = "Retrieves all credit applications for a specific affiliate")
    public ResponseEntity<List<CreditApplicationResponse>> getCreditApplicationsByAffiliate(
            @PathVariable Long affiliateId) {
        List<CreditApplication> applications = retrieveCreditApplicationUseCase.getCreditApplicationsByAffiliate(affiliateId);
        List<CreditApplicationResponse> responses = applications.stream()
                .map(creditApplicationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Get all credit applications with pagination.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    public ResponseEntity<List<CreditApplicationResponse>> getAllCreditApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<CreditApplication> applications = retrieveCreditApplicationUseCase.getAllCreditApplications(page, size);
        List<CreditApplicationResponse> responses = applications.stream()
                .map(creditApplicationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Get credit applications by status.
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    public ResponseEntity<List<CreditApplicationResponse>> getCreditApplicationsByStatus(
            @PathVariable CreditApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<CreditApplication> applications = retrieveCreditApplicationUseCase.getCreditApplicationsByStatus(status, page, size);
        List<CreditApplicationResponse> responses = applications.stream()
                .map(creditApplicationMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Start review of a credit application.
     */
    @PostMapping("/{id}/start-review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    public ResponseEntity<CreditApplicationResponse> startReview(@PathVariable Long id) {
        CreditApplication creditApplication = processCreditDecisionUseCase.startReview(id);
        return ResponseEntity.ok(creditApplicationMapper.toResponse(creditApplication));
    }

    /**
     * Process a credit decision (approve/reject).
     */
    @PostMapping("/{id}/decision")
    @PreAuthorize("hasRole('ANALISTA')")
    @Operation(summary = "Process credit decision", description = "Approves or rejects a credit application")
    public ResponseEntity<CreditApplicationResponse> processCreditDecision(
            @PathVariable Long id,
            @Valid @RequestBody CreditDecisionRequest request) {
        CreditApplication creditApplication = processCreditDecisionUseCase.approveCreditApplication(id, request.getComments());
        return ResponseEntity.ok(creditApplicationMapper.toResponse(creditApplication));
    }

    /**
     * Reject a credit application.
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    public ResponseEntity<CreditApplicationResponse> rejectCreditApplication(
            @PathVariable Long id,
            @Valid @RequestBody CreditDecisionRequest request) {
        CreditApplication creditApplication = processCreditDecisionUseCase.rejectCreditApplication(id, request.getComments());
        return ResponseEntity.ok(creditApplicationMapper.toResponse(creditApplication));
    }

    /**
     * Cancel a credit application.
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA') or hasRole('AFILIADO')")
    public ResponseEntity<CreditApplicationResponse> cancelCreditApplication(
            @PathVariable Long id,
            @Valid @RequestBody CreditDecisionRequest request) {
        CreditApplication creditApplication = processCreditDecisionUseCase.cancelCreditApplication(id, request.getComments());
        return ResponseEntity.ok(creditApplicationMapper.toResponse(creditApplication));
    }
}
