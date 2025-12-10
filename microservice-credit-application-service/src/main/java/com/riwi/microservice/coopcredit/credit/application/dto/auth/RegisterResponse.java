package com.riwi.microservice.coopcredit.credit.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO for register response (without JWT token).
 */
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

    public RegisterResponse() {
    }

    public RegisterResponse(Long id, String username, String email, 
                          String firstName, String lastName, List<String> roles, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
