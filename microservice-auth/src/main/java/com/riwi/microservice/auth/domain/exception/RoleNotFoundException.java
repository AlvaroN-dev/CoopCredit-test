package com.riwi.microservice.auth.domain.exception;

/**
 * Exception thrown when a role is not found.
 */
public class RoleNotFoundException extends RuntimeException {
    
    public RoleNotFoundException(String roleName) {
        super("Role not found: " + roleName);
    }
}
