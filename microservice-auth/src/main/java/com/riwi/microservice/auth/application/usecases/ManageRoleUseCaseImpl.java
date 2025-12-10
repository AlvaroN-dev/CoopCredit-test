package com.riwi.microservice.auth.application.usecases;

import com.riwi.microservice.auth.domain.exception.RoleNotFoundException;
import com.riwi.microservice.auth.domain.exception.UserNotFoundException;
import com.riwi.microservice.auth.domain.models.Role;
import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.ManageRoleUseCase;
import com.riwi.microservice.auth.domain.port.out.RoleRepositoryPort;
import com.riwi.microservice.auth.domain.port.out.UserRepositoryPort;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ManageRoleUseCaseImpl implements ManageRoleUseCase {

    private final UserRepositoryPort userRepository;
    private final RoleRepositoryPort roleRepository;

    public ManageRoleUseCaseImpl(UserRepositoryPort userRepository, RoleRepositoryPort roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
        
        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
        user.setRoles(roles);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }

    @Override
    public User removeRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        if (user.getRoles() != null) {
            user.getRoles().removeIf(role -> role.getName().equals(roleName));
            user.setUpdatedAt(LocalDateTime.now());
        }
        
        return userRepository.save(user);
    }
}
