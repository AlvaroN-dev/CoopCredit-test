package com.riwi.microservice.auth.domain.port.in;

import com.riwi.microservice.auth.domain.models.User;

/**
 * Use case for managing user roles.
 * Single Responsibility: Only handles role assignment/removal.
 */
public interface ManageRoleUseCase {
    
    /**
     * Assign a role to a user
     * @param userId the user ID
     * @param roleName the role name to assign
     * @return the updated user
     */
    User assignRole(Long userId, String roleName);
    
    /**
     * Remove a role from a user
     * @param userId the user ID
     * @param roleName the role name to remove
     * @return the updated user
     */
    User removeRole(Long userId, String roleName);
}
