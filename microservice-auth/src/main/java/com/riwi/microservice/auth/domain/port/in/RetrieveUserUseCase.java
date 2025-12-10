package com.riwi.microservice.auth.domain.port.in;

import com.riwi.microservice.auth.domain.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Use case for retrieving users.
 * Single Responsibility: Only handles user retrieval operations.
 */
public interface RetrieveUserUseCase {
    
    /**
     * Find a user by their ID
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> findById(Long id);
    
    /**
     * Find a user by their username
     * @param username the username
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by their email
     * @param email the email
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Get all users
     * @return list of all users
     */
    List<User> findAll();
    
    /**
     * Check if username exists
     * @param username the username to check
     * @return true if exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     * @param email the email to check
     * @return true if exists
     */
    boolean existsByEmail(String email);
}
