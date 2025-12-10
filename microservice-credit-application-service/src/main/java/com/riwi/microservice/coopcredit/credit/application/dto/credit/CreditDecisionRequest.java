package com.riwi.microservice.coopcredit.credit.application.dto.credit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for credit application decisions.
 */
@Schema(description = "Request DTO for credit application decisions")
public class CreditDecisionRequest {

    @Size(max = 1000, message = "Los comentarios no pueden exceder 1000 caracteres")
    @Schema(description = "Comments or notes regarding the decision", example = "Approved based on good credit history")
    private String comments;

    // Getters and Setters
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
