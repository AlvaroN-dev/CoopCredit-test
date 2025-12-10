package com.riwi.microservice.auth.domain.models;

/**
 * Enum defining the available roles in the system.
 */
public enum RoleType {
    ROLE_AFILIADO("Affiliated member role"),
    ROLE_ANALISTA("Analyst role for credit analysis"),
    ROLE_ADMIN("Administrator role with full access");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getRoleName() {
        return this.name();
    }
}
