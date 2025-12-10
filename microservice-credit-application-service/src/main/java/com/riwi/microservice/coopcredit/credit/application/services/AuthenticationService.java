package com.riwi.microservice.coopcredit.credit.application.services;

import com.riwi.microservice.coopcredit.credit.application.dto.auth.LoginRequest;
import com.riwi.microservice.coopcredit.credit.application.dto.auth.LoginResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.auth.RegisterRequest;
import com.riwi.microservice.coopcredit.credit.application.dto.auth.RegisterResponse;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.RoleEntity;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.UserEntity;
import com.riwi.microservice.coopcredit.credit.infrastructure.metrics.AuthenticationMetrics;
import com.riwi.microservice.coopcredit.credit.infrastructure.repositories.JpaUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for authentication operations.
 */
@Service
public class AuthenticationService {

    private final JpaUserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationMetrics authenticationMetrics;
    private final SecretKey secretKey;
    private final Long expirationTime;

    public AuthenticationService(
            JpaUserRepository userRepository,
            RoleService roleService,
            PasswordEncoder passwordEncoder,
            AuthenticationMetrics authenticationMetrics,
            @Value("${JWT_SECRET}") String secret,
            @Value("${JWT_EXPIRATION}") Long expirationTime) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationMetrics = authenticationMetrics;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    /**
     * Authenticate user and generate JWT token.
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        try {
            UserEntity user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> {
                        authenticationMetrics.recordLoginFailure();
                        return new RuntimeException("Invalid credentials");
                    });

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                authenticationMetrics.recordLoginFailure();
                throw new RuntimeException("Invalid credentials");
            }

            if (!user.isEnabled()) {
                authenticationMetrics.recordLoginFailure();
                throw new RuntimeException("User account is disabled");
            }

            String token = generateToken(user);
            
            // Return roles WITHOUT "ROLE_" prefix (ADMIN, ANALISTA, AFILIADO)
            List<String> roles = user.getRoles().stream()
                    .map(role -> role.getName().replace("ROLE_", ""))
                    .collect(Collectors.toList());

            authenticationMetrics.recordLoginSuccess();
            return new LoginResponse(token, expirationTime, user.getUsername(), roles);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Register new user (NO JWT token returned).
     */
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);

        // Assign role dynamically (creates if doesn't exist)
        String roleName = request.getRole() != null ? request.getRole() : "AFILIADO";
        RoleEntity role = roleService.getOrCreateRole(roleName);

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        user = userRepository.save(user);

        // Return roles WITHOUT "ROLE_" prefix (ADMIN, ANALISTA, AFILIADO)
        List<String> roleNames = user.getRoles().stream()
                .map(r -> r.getName().replace("ROLE_", ""))
                .collect(Collectors.toList());

        authenticationMetrics.recordRegistration();
        
        return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roleNames,
                user.isEnabled()
        );
    }

    /**
     * Get current user information from token.
     */
    @Transactional(readOnly = true)
    public RegisterResponse getCurrentUser(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Return roles WITHOUT "ROLE_" prefix (ADMIN, ANALISTA, AFILIADO)
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toList());

        return new RegisterResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roles,
                user.isEnabled()
        );
    }

    /**
     * Generate JWT token for user.
     */
    private String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        // Store roles WITHOUT "ROLE_" prefix - the filter will add it
        claims.put("roles", user.getRoles().stream()
                .map(role -> role.getName().replace("ROLE_", ""))
                .collect(Collectors.toList()));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .header().type("JWT").and()
                .id(UUID.randomUUID().toString())
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validate JWT token.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get username from JWT token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
}
