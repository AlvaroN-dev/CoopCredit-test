package com.riwi.microservice.coopcredit.credit.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT Authentication Filter for validating JWT tokens.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final Counter authFailureCounter;

    public JwtAuthenticationFilter(MeterRegistry meterRegistry) {
        this.authFailureCounter = Counter.builder("security.authentication.failures")
                .description("Count of authentication failures")
                .register(meterRegistry);
    }

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String jwt = extractJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) && validateToken(jwt)) {
                Claims claims = extractAllClaims(jwt);
                String username = claims.getSubject();
                
                @SuppressWarnings("unchecked")
                List<String> roles = claims.get("roles", List.class);
                
                if (roles != null && !roles.isEmpty()) {
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.replace("ROLE_", "")))
                            .collect(Collectors.toList());
                    
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            authFailureCounter.increment();
            logger.warn("JWT token is expired: " + e.getMessage());
        } catch (MalformedJwtException e) {
            authFailureCounter.increment();
            logger.warn("Invalid JWT token: " + e.getMessage());
        } catch (SecurityException e) {
            authFailureCounter.increment();
            logger.warn("JWT signature validation failed: " + e.getMessage());
        } catch (Exception e) {
            authFailureCounter.increment();
            logger.error("Cannot set user authentication: " + e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") || 
               path.startsWith("/swagger-ui") || 
               path.startsWith("/v3/api-docs");
    }
}
