package com.riwi.microservice.auth.domain.port.in;

import com.riwi.microservice.auth.domain.models.User;

/**
 * Use case for user registration.
 * Single Responsibility: Only handles user registration.
 */
public interface RegisterUserUseCase {
    
    /**
     * Register a new user in the system
     * @param user the user to register
     * @param rawPassword the raw password (will be encoded)
     * @param roleName the role to assign
     * @return the registered user
     */
    User register(User user, String rawPassword, String roleName);
}
