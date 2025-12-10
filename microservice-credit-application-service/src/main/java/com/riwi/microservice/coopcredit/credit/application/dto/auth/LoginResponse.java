package com.riwi.microservice.coopcredit.credit.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO for login response (with JWT token).
 */
@Getter
@Setter
@Schema(description = "Response after successful authentication")
public class LoginResponse {

    @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;

    @Schema(description = "Token type", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Token expiration time in milliseconds", example = "86400000")
    private Long expiresIn;

    @Schema(description = "Username", example = "jperez")
    private String username;

    @Schema(description = "User roles without ROLE_ prefix", example = "[\"AFILIADO\"]")
    private List<String> roles;


    public LoginResponse(String token, Long expiresIn, String username, List<String> roles) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.username = username;
        this.roles = roles;
    }

}
