package com.riwi.microservice.auth.domain.port.in;

import com.riwi.microservice.auth.domain.models.User;

/**
 * Use case for updating users.
 * Single Responsibility: Only handles user update operations.
 */
public interface UpdateUserUseCase {
    
    /**
     * Update user information
     * @param user the user with updated information
     * @return the updated user
     */
    User update(User user);
    
    /**
     * Enable or disable a user
     * @param userId the user ID
     * @param enabled true to enable, false to disable
     * @return the updated user
     */
    User setEnabled(Long userId, boolean enabled);
}
