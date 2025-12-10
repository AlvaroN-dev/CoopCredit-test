package com.riwi.microservice.auth.infrastructure.security;

import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.out.JwtTokenPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementing JwtTokenPort for JWT operations.
 */
@Component
public class JwtTokenAdapter implements JwtTokenPort {

    private final SecretKey secretKey;
    private final Long expirationTime;

    public JwtTokenAdapter(
            @Value("${JWT_SECRET}") String secret,
            @Value("${JWT_EXPIRATION}") Long expirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        
        if (user.getRoles() != null) {
            claims.put("roles", user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toList()));
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .header().type("JWT").and() // Best practice: Explicitly set type
                .id(UUID.randomUUID().toString()) // Best practice: Add JTI (JWT ID) for replay protection
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    @Override
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

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    @Override
    public Map<String, Object> getClaimsFromToken(String token) {
        Claims claims = getClaims(token);
        return new HashMap<>(claims);
    }

    @Override
    public Long getExpirationTime() {
        return expirationTime / 1000; // Return in seconds
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
