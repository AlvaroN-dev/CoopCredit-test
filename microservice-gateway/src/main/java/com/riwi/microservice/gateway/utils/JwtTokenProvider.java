package com.riwi.microservice.gateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Utility class for JWT token operations in the Gateway.
 */
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(
            @Value("${jwt.secret:mySecretKeyForJwtTokenGenerationThatIsAtLeast256BitsLongAndSecure}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Validate a JWT token.
     * @param token the token to validate
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Get username from token.
     * @param token the token
     * @return the username (subject)
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    /**
     * Get user ID from token.
     * @param token the token
     * @return the user ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        Object userId = claims.get("userId");
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        return null;
    }

    /**
     * Get roles from token.
     * @param token the token
     * @return list of role names
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = getClaims(token);
        return (List<String>) claims.get("roles");
    }

    /**
     * Check if token has a specific role.
     * @param token the token
     * @param role the role to check
     * @return true if token contains the role
     */
    public boolean hasRole(String token, String role) {
        List<String> roles = getRolesFromToken(token);
        return roles != null && roles.contains(role);
    }

    /**
     * Check if token has any of the specified roles.
     * @param token the token
     * @param roles the roles to check
     * @return true if token contains any of the roles
     */
    public boolean hasAnyRole(String token, List<String> roles) {
        List<String> tokenRoles = getRolesFromToken(token);
        if (tokenRoles == null) {
            return false;
        }
        return roles.stream().anyMatch(tokenRoles::contains);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
