package com.riwi.microservice.coopcredit.credit.application.dto.credit;

import com.riwi.microservice.coopcredit.credit.application.dto.affiliate.AffiliateResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.risk.RiskEvaluationResponse;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for Credit Application.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response DTO for Credit Application")
public class CreditApplicationResponse {
    
    @Schema(description = "Unique ID of the application", example = "1")
    private Long id;
    @Schema(description = "Application number", example = "APP-2023-001")
    private String applicationNumber;
    @Schema(description = "Requested amount", example = "5000000")
    private BigDecimal requestedAmount;
    @Schema(description = "Term in months", example = "12")
    private Integer termMonths;
    @Schema(description = "Interest rate", example = "1.5")
    private BigDecimal interestRate;
    @Schema(description = "Monthly payment amount", example = "450000")
    private BigDecimal monthlyPayment;
    @Schema(description = "Purpose of the credit", example = "Home improvement")
    private String purpose;
    @Schema(description = "Status of the application", example = "APPROVED")
    private CreditApplicationStatus status;
    @Schema(description = "Comments or notes", example = "Approved after review")
    private String comments;
    @Schema(description = "Date of application")
    private LocalDateTime applicationDate;
    @Schema(description = "Date of review")
    private LocalDateTime reviewDate;
    @Schema(description = "Date of decision")
    private LocalDateTime decisionDate;
    @Schema(description = "Record creation timestamp")
    private LocalDateTime createdAt;
    @Schema(description = "Record update timestamp")
    private LocalDateTime updatedAt;
    @Schema(description = "Affiliate details")
    private AffiliateResponse affiliate;
    @Schema(description = "Risk evaluation details")
    private RiskEvaluationResponse riskEvaluation;
}
