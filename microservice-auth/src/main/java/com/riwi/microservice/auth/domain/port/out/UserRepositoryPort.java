package com.riwi.microservice.auth.domain.port.out;

import com.riwi.microservice.auth.domain.models.User;

import java.util.List;
import java.util.Optional;

/**
 * Output port for user persistence operations.
 * This interface will be implemented by the infrastructure layer.
 */
public interface UserRepositoryPort {
    
    /**
     * Save a user (create or update)
     * @param user the user to save
     * @return the saved user
     */
    User save(User user);
    
    /**
     * Find a user by ID
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> findById(Long id);
    
    /**
     * Find a user by username
     * @param username the username
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email
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
    
    /**
     * Delete a user by ID
     * @param id the user ID
     */
    void deleteById(Long id);
}
