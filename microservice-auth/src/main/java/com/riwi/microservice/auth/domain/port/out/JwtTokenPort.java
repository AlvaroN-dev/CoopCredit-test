package com.riwi.microservice.auth.domain.port.out;

import com.riwi.microservice.auth.domain.models.User;

import java.util.Map;

/**
 * Output port for JWT token operations.
 */
public interface JwtTokenPort {
    
    /**
     * Generate a JWT token for a user
     * @param user the user
     * @return the generated token
     */
    String generateToken(User user);
    
    /**
     * Validate a JWT token
     * @param token the token to validate
     * @return true if valid
     */
    boolean validateToken(String token);
    
    /**
     * Get username from token
     * @param token the token
     * @return the username
     */
    String getUsernameFromToken(String token);
    
    /**
     * Get all claims from token
     * @param token the token
     * @return map of claims
     */
    Map<String, Object> getClaimsFromToken(String token);
    
    /**
     * Get token expiration time in seconds
     * @return expiration time
     */
    Long getExpirationTime();
}
