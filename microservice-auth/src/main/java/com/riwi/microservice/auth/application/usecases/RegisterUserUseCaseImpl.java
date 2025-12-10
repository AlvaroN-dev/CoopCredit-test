package com.riwi.microservice.auth.application.usecases;

import com.riwi.microservice.auth.domain.exception.RoleNotFoundException;
import com.riwi.microservice.auth.domain.exception.UserAlreadyExistsException;
import com.riwi.microservice.auth.domain.models.Role;
import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.RegisterUserUseCase;
import com.riwi.microservice.auth.domain.port.out.PasswordEncoderPort;
import com.riwi.microservice.auth.domain.port.out.RoleRepositoryPort;
import com.riwi.microservice.auth.domain.port.out.UserRepositoryPort;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final RoleRepositoryPort roleRepository;
    private final PasswordEncoderPort passwordEncoder;

    public RegisterUserUseCaseImpl(UserRepositoryPort userRepository, RoleRepositoryPort roleRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user, String rawPassword, String roleName) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + user.getEmail());
        }

        // Find the role
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        // Set user properties
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}
