package com.riwi.microservice.coopcredit.credit.infrastructure.controller;

import com.riwi.microservice.coopcredit.credit.application.dto.auth.LoginRequest;
import com.riwi.microservice.coopcredit.credit.application.dto.auth.LoginResponse;
import com.riwi.microservice.coopcredit.credit.application.dto.auth.RegisterRequest;
import com.riwi.microservice.coopcredit.credit.application.dto.auth.RegisterResponse;
import com.riwi.microservice.coopcredit.credit.application.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication endpoints.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "🔐 Authentication", description = "Autenticación y registro de usuarios")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(
            summary = "Login de usuario", 
            description = "Autentica un usuario y devuelve un token JWT válido por 24 horas. No requiere autenticación previa."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso - Token JWT generado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Registro de usuario", 
            description = "Registra un nuevo usuario en el sistema. NO devuelve token JWT, solo los datos del usuario creado. No requiere autenticación."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro exitoso - Usuario creado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Obtener usuario actual", 
            description = "Obtiene la información del usuario autenticado. Requiere token JWT válido.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Información del usuario obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado - Token inválido o expirado")
    })
    @GetMapping("/me")
    public ResponseEntity<RegisterResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RegisterResponse userInfo = authenticationService.getCurrentUser(username);
        return ResponseEntity.ok(userInfo);
    }
}
