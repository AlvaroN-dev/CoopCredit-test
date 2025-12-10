package com.riwi.microservice.auth.application.dto;

import java.util.List;


/**
 * DTO for token validation response.
 */
public class TokenValidationResponse {

    private boolean valid;
    private String username;
    private Long userId;
    private List<String> roles;
    private String message;

    public TokenValidationResponse() {
    }

    public TokenValidationResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public TokenValidationResponse(boolean valid, String username, Long userId, List<String> roles) {
        this.valid = valid;
        this.username = username;
        this.userId = userId;
        this.roles = roles;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
