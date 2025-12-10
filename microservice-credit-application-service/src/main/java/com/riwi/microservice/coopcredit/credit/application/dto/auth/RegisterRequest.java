package com.riwi.microservice.coopcredit.credit.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for registration request.
 */

@Getter
@Setter
@Schema(description = "DTO for registration request")
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Username", example = "john.doe")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Schema(description = "Password", example = "password123")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Size(max = 50, message = "First name must be at most 50 characters")
    @Schema(description = "First name", example = "John")
    private String firstName;

    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Role", example = "ROLE_AFILIADO")
    private String role = "ROLE_AFILIADO"; // Default role

}
