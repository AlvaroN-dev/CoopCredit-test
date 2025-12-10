package com.riwi.microservice.coopcredit.credit.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for login request.
 */

@Getter
@Setter
@Schema(description = "DTO for login request")
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Schema(description = "Username", example = "john.doe")
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "Password", example = "password123")
    private String password;

}
