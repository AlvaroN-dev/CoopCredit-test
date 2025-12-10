package com.riwi.microservice.auth.domain.port.out;

import com.riwi.microservice.auth.domain.models.Role;

import java.util.Optional;
import java.util.Set;

/**
 * Output port for role persistence operations.
 */
public interface RoleRepositoryPort {
    
    /**
     * Find a role by name
     * @param name the role name
     * @return Optional containing the role if found
     */
    Optional<Role> findByName(String name);
    
    /**
     * Get all roles
     * @return set of all roles
     */
    Set<Role> findAll();
    
    /**
     * Save a role
     * @param role the role to save
     * @return the saved role
     */
    Role save(Role role);
}
