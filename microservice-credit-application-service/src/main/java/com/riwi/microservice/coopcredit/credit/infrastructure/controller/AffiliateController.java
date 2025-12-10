package com.riwi.microservice.coopcredit.credit.infrastructure.controller;

import com.riwi.microservice.coopcredit.credit.application.dto.affiliate.AffiliateResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.affiliate.CreateAffiliateRequest;
import com.riwi.microservice.coopcredit.credit.application.dto.affiliate.UpdateAffiliateRequest;
import com.riwi.microservice.coopcredit.credit.application.mapper.AffiliateMapper;
import com.riwi.microservice.coopcredit.credit.domain.exception.AffiliateNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.DeleteAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.ManageAffiliateStatusUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.RetrieveAffiliateUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.in.UpdateAffiliateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
 * REST Controller for Affiliate operations.
 */
@RestController
@RequestMapping("/credit/affiliates")
@Tag(name = "👥 Afiliados", description = "Gestión de afiliados cooperativos")
@SecurityRequirement(name = "bearerAuth")
public class AffiliateController {

    private final CreateAffiliateUseCase createAffiliateUseCase;
    private final RetrieveAffiliateUseCase retrieveAffiliateUseCase;
    private final UpdateAffiliateUseCase updateAffiliateUseCase;
    private final ManageAffiliateStatusUseCase manageAffiliateStatusUseCase;
    private final DeleteAffiliateUseCase deleteAffiliateUseCase;
    private final AffiliateMapper affiliateMapper;

    public AffiliateController(CreateAffiliateUseCase createAffiliateUseCase,
                               RetrieveAffiliateUseCase retrieveAffiliateUseCase,
                               UpdateAffiliateUseCase updateAffiliateUseCase,
                               ManageAffiliateStatusUseCase manageAffiliateStatusUseCase,
                               DeleteAffiliateUseCase deleteAffiliateUseCase,
                               AffiliateMapper affiliateMapper) {
        this.createAffiliateUseCase = createAffiliateUseCase;
        this.retrieveAffiliateUseCase = retrieveAffiliateUseCase;
        this.updateAffiliateUseCase = updateAffiliateUseCase;
        this.manageAffiliateStatusUseCase = manageAffiliateStatusUseCase;
        this.deleteAffiliateUseCase = deleteAffiliateUseCase;
        this.affiliateMapper = affiliateMapper;
    }

    /**
     * Create a new affiliate.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Create a new affiliate", description = "Registers a new affiliate in the system")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Afiliado creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AffiliateResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de afiliado inválidos",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "El afiliado ya existe",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<AffiliateResponse> createAffiliate(@Valid @RequestBody CreateAffiliateRequest request) {
        Affiliate affiliate = createAffiliateUseCase.createAffiliate(affiliateMapper.toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(affiliateMapper.toResponse(affiliate));
    }

    /**
     * Get affiliate by ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA') or hasRole('AFILIADO')")
    @Operation(summary = "Get affiliate by ID", description = "Retrieves an affiliate by their unique ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Afiliado encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AffiliateResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Afiliado no encontrado",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<AffiliateResponse> getAffiliateById(@PathVariable Long id) {
        Affiliate affiliate = retrieveAffiliateUseCase.getAffiliateById(id)
                .orElseThrow(() -> new AffiliateNotFoundException(id));
        return ResponseEntity.ok(affiliateMapper.toResponse(affiliate));
    }

    /**
     * Get affiliate by document.
     */
    @GetMapping("/document/{document}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Get affiliate by document", description = "Retrieves an affiliate by their document number")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Afiliado encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AffiliateResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Afiliado no encontrado",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<AffiliateResponse> getAffiliateByDocument(@PathVariable String document) {
        Affiliate affiliate = retrieveAffiliateUseCase.getAffiliateByDocument(document)
                .orElseThrow(() -> new AffiliateNotFoundException(document));
        return ResponseEntity.ok(affiliateMapper.toResponse(affiliate));
    }

    /**
     * Get all affiliates with pagination.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Get all affiliates", description = "Retrieves a paginated list of all affiliates")
    public ResponseEntity<List<AffiliateResponse>> getAllAffiliates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Affiliate> affiliates = retrieveAffiliateUseCase.getAllAffiliates(page, size);
        List<AffiliateResponse> responses = affiliates.stream()
                .map(affiliateMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Get affiliates by status.
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Get affiliates by status", description = "Retrieves a paginated list of affiliates filtered by status")
    public ResponseEntity<List<AffiliateResponse>> getAffiliatesByStatus(
            @PathVariable AffiliateStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Affiliate> affiliates = retrieveAffiliateUseCase.getAffiliatesByStatus(status, page, size);
        List<AffiliateResponse> responses = affiliates.stream()
                .map(affiliateMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    /**
     * Update an affiliate.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALISTA')")
    @Operation(summary = "Update an affiliate", description = "Updates the information of an existing affiliate")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Afiliado actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AffiliateResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de afiliado inválidos",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Afiliado no encontrado",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<AffiliateResponse> updateAffiliate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAffiliateRequest request) {
        Affiliate affiliate = updateAffiliateUseCase.updateAffiliate(id, affiliateMapper.toUpdateCommand(request));
        return ResponseEntity.ok(affiliateMapper.toResponse(affiliate));
    }

    /**
     * Change affiliate status.
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change affiliate status", description = "Updates the status of an affiliate")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Estado de afiliado actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AffiliateResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Afiliado no encontrado",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<AffiliateResponse> changeAffiliateStatus(
            @PathVariable Long id,
            @RequestParam AffiliateStatus status) {
        Affiliate affiliate = manageAffiliateStatusUseCase.changeAffiliateStatus(id, status);
        return ResponseEntity.ok(affiliateMapper.toResponse(affiliate));
    }

    /**
     * Delete an affiliate (soft delete).
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete an affiliate", description = "Performs a soft delete of an affiliate")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Afiliado eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Afiliado no encontrado",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<Void> deleteAffiliate(@PathVariable Long id) {
        deleteAffiliateUseCase.deleteAffiliate(id);
        return ResponseEntity.noContent().build();
    }
}
