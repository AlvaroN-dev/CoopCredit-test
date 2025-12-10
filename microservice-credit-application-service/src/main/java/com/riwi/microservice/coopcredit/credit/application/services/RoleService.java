package com.riwi.microservice.coopcredit.credit.application.services;

import com.riwi.microservice.coopcredit.credit.infrastructure.entities.RoleEntity;
import com.riwi.microservice.coopcredit.credit.infrastructure.repositories.JpaRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing roles dynamically.
 */
@Service
public class RoleService {

    private final JpaRoleRepository roleRepository;

    public RoleService(JpaRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Get all roles.
     */
    @Transactional(readOnly = true)
    public List<RoleEntity> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Get role by name.
     */
    @Transactional(readOnly = true)
    public Optional<RoleEntity> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Get or create role.
     * If role doesn't exist, create it dynamically.
     */
    @Transactional
    public RoleEntity getOrCreateRole(String roleName) {
        // Ensure role name has ROLE_ prefix
        String normalizedRoleName = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
        
        return roleRepository.findByName(normalizedRoleName)
                .orElseGet(() -> {
                    RoleEntity newRole = new RoleEntity();
                    newRole.setName(normalizedRoleName);
                    newRole.setDescription("Dynamically created role");
                    return roleRepository.save(newRole);
                });
    }

    /**
     * Create a new role.
     */
    @Transactional
    public RoleEntity createRole(String roleName, String description) {
        // Ensure role name has ROLE_ prefix
        String normalizedRoleName = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
        
        if (roleRepository.findByName(normalizedRoleName).isPresent()) {
            throw new RuntimeException("Role already exists: " + normalizedRoleName);
        }

        RoleEntity role = new RoleEntity();
        role.setName(normalizedRoleName);
        role.setDescription(description);
        return roleRepository.save(role);
    }

    /**
     * Delete a role by name.
     */
    @Transactional
    public void deleteRole(String roleName) {
        RoleEntity role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        roleRepository.delete(role);
    }
}
