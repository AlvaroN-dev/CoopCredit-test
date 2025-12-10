package com.riwi.microservice.auth.infrastructure.controller;

import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.AuthenticateUserUseCase;
import com.riwi.microservice.auth.domain.port.in.AuthenticateUserUseCase.AuthenticationResult;
import com.riwi.microservice.auth.domain.port.in.RefreshTokenUseCase;
import com.riwi.microservice.auth.domain.port.in.RegisterUserUseCase;
import com.riwi.microservice.auth.domain.port.in.ValidateTokenUseCase;
import com.riwi.microservice.auth.domain.port.out.JwtTokenPort;
import com.riwi.microservice.auth.application.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication and registration operations")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final JwtTokenPort jwtTokenPort;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase,
                          RegisterUserUseCase registerUserUseCase,
                          ValidateTokenUseCase validateTokenUseCase,
                          RefreshTokenUseCase refreshTokenUseCase,
                          JwtTokenPort jwtTokenPort) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.registerUserUseCase = registerUserUseCase;
        this.validateTokenUseCase = validateTokenUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.jwtTokenPort = jwtTokenPort;
    }

    /**
     * Authenticate a user and return a JWT token.
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthenticationResult result = authenticateUserUseCase.authenticate(
                request.getUsername(), 
                request.getPassword()
        );

        AuthResponse response = mapToAuthResponse(result);
        return ResponseEntity.ok(response);
    }

    /**
     * Register a new user.
     */
    @PostMapping("/register")
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea una nueva cuenta de usuario y devuelve tokens de autenticación"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de registro inválidos",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "El email ya está registrado",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());

        // Register the user
        registerUserUseCase.register(
                newUser, 
                request.getPassword(), 
                request.getRole()
        );

        // Auto-login after registration
        AuthenticationResult result = authenticateUserUseCase.authenticate(
                request.getUsername(), 
                request.getPassword()
        );

        AuthResponse response = mapToAuthResponse(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Validate a JWT token.
     */
    @PostMapping("/validate")
    @Operation(summary = "Validate Token", description = "Validates a JWT token")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token validado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = TokenValidationResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Token inválido o mal formado",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<TokenValidationResponse> validateToken(@Valid @RequestBody TokenValidationRequest request) {
        boolean isValid = validateTokenUseCase.validateToken(request.getToken());
        
        if (!isValid) {
            return ResponseEntity.ok(new TokenValidationResponse(false, "Invalid or expired token"));
        }

        Map<String, Object> claims = jwtTokenPort.getClaimsFromToken(request.getToken());
        String username = jwtTokenPort.getUsernameFromToken(request.getToken());
        
        Long userId = null;
        if (claims.get("userId") != null) {
            userId = ((Number) claims.get("userId")).longValue();
        }
        
        @SuppressWarnings("unchecked")
        List<String> roles = (List<String>) claims.get("roles");

        TokenValidationResponse response = new TokenValidationResponse(true, username, userId, roles);
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh an existing valid token.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        
        AuthenticationResult result = refreshTokenUseCase.refreshToken(token);
        AuthResponse response = mapToAuthResponse(result);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get current user information from token.
     */
    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        
        return validateTokenUseCase.getUserFromToken(token)
                .map(user -> ResponseEntity.ok(mapToUserDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }

    private AuthResponse mapToAuthResponse(AuthenticationResult result) {
        AuthResponse response = new AuthResponse();
        response.setAccessToken(result.accessToken());
        response.setTokenType(result.tokenType());
        response.setExpiresIn(result.expiresIn());
        response.setUser(mapToUserDto(result.user()));
        return response;
    }

    private AuthResponse.UserDto mapToUserDto(User user) {
        List<String> roleNames = user.getRoles() != null
                ? user.getRoles().stream()
                    .map(role -> role.getName())
                    .collect(Collectors.toList())
                : List.of();

        return new AuthResponse.UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roleNames
        );
    }
}
