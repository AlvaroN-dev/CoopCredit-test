package com.riwi.microservice.auth.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Domain entity representing a User in the system.
 * This is a pure domain object without any framework dependencies.
 */
public class User {
    
    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private Set<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(Long id, String username, String password, String email, 
                String firstName, String lastName, boolean enabled, 
                Set<Role> roles, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.roles = roles;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Check if user has a specific role
     */
    public boolean hasRole(String roleName) {
        return roles != null && roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }
}
