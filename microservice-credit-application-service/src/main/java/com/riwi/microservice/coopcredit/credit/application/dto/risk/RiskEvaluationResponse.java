package com.riwi.microservice.coopcredit.credit.application.dto.risk;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for Risk Evaluation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluationResponse {
    
    private Long id;
    private Integer creditScore;
    private RiskLevel riskLevel;
    private BigDecimal debtToIncomeRatio;
    private Boolean hasDefaultHistory;
    private Integer yearsEmployed;
    private Boolean hasGuarantor;
    private BigDecimal collateralValue;
    private String evaluationNotes;
    private String recommendation;
    private Boolean approved;
    private String evaluatedBy;
    private LocalDateTime evaluationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer compositeRiskScore;

}
