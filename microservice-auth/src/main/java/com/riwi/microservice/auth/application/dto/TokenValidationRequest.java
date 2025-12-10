package com.riwi.microservice.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for token validation request.
 */
public class TokenValidationRequest {

    @NotBlank(message = "Token is required")
    private String token;

    public TokenValidationRequest() {
    }

    public TokenValidationRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
