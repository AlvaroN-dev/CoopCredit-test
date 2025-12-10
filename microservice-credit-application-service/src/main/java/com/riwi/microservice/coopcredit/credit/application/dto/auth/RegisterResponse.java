package com.riwi.microservice.coopcredit.credit.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for register response (without JWT token).
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response after successful user registration")
public class RegisterResponse {

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "Username", example = "jperez")
    private String username;

    @Schema(description = "Email address", example = "jperez@coopcredit.com")
    private String email;

    @Schema(description = "First name", example = "Juan")
    private String firstName;

    @Schema(description = "Last name", example = "Pérez")
    private String lastName;

    @Schema(description = "User roles without ROLE_ prefix", example = "[\"AFILIADO\"]")
    private List<String> roles;

    @Schema(description = "Account status", example = "true")
    private boolean enabled;

}
