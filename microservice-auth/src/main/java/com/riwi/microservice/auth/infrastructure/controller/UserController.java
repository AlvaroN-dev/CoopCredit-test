package com.riwi.microservice.auth.infrastructure.controller;

import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.ManageRoleUseCase;
import com.riwi.microservice.auth.domain.port.in.RetrieveUserUseCase;
import com.riwi.microservice.auth.domain.port.in.UpdateUserUseCase;
import com.riwi.microservice.auth.application.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for user management endpoints.
 * Only accessible by administrators.
 */
@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations for managing users (Admin only)")
public class UserController {

    private final RetrieveUserUseCase retrieveUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final ManageRoleUseCase manageRoleUseCase;

    public UserController(RetrieveUserUseCase retrieveUserUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          ManageRoleUseCase manageRoleUseCase) {
        this.retrieveUserUseCase = retrieveUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.manageRoleUseCase = manageRoleUseCase;
    }

    /**
     * Get all users (Admin only).
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios recuperada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.UserDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "No autorizado para ver usuarios",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<List<AuthResponse.UserDto>> getAllUsers() {
        List<AuthResponse.UserDto> users = retrieveUserUseCase.findAll().stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    /**
     * Get a user by ID (Admin only).
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique ID")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuario encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.UserDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado",
            content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemDetail.class)
            )
        )
    })
    public ResponseEntity<AuthResponse.UserDto> getUserById(@PathVariable Long id) {
        return retrieveUserUseCase.findById(id)
                .map(user -> ResponseEntity.ok(mapToUserDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Enable or disable a user (Admin only).
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Set user status", description = "Enables or disables a user account")
    public ResponseEntity<AuthResponse.UserDto> setUserStatus(
            @PathVariable Long id,
            @RequestParam boolean enabled) {
        User user = updateUserUseCase.setEnabled(id, enabled);
        return ResponseEntity.ok(mapToUserDto(user));
    }

    /**
     * Assign a role to a user (Admin only).
     */
    @PostMapping("/{id}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign role", description = "Assigns a role to a user")
    public ResponseEntity<AuthResponse.UserDto> assignRole(
            @PathVariable Long id,
            @PathVariable String roleName) {
        User user = manageRoleUseCase.assignRole(id, roleName);
        return ResponseEntity.ok(mapToUserDto(user));
    }

    /**
     * Remove a role from a user (Admin only).
     */
    @DeleteMapping("/{id}/roles/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove role", description = "Removes a role from a user")
    public ResponseEntity<AuthResponse.UserDto> removeRole(
            @PathVariable Long id,
            @PathVariable String roleName) {
        User user = manageRoleUseCase.removeRole(id, roleName);
        return ResponseEntity.ok(mapToUserDto(user));
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
