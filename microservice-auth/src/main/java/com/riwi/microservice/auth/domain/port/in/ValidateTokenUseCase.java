package com.riwi.microservice.auth.domain.port.in;

import com.riwi.microservice.auth.domain.models.User;

import java.util.Optional;

/**
 * Use case for token validation.
 * Single Responsibility: Only handles token validation operations.
 */
public interface ValidateTokenUseCase {
    
    /**
     * Validate a JWT token
     * @param token the JWT token
     * @return true if valid, false otherwise
     */
    boolean validateToken(String token);
    
    /**
     * Get user from token
     * @param token the JWT token
     * @return Optional containing the user if found
     */
    Optional<User> getUserFromToken(String token);
}
