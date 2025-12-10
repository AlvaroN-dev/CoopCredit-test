package com.riwi.microservice.auth.application.services;

import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.ManageRoleUseCase;
import com.riwi.microservice.auth.domain.port.in.RetrieveUserUseCase;
import com.riwi.microservice.auth.domain.port.in.UpdateUserUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserManagementService implements RetrieveUserUseCase, UpdateUserUseCase, ManageRoleUseCase {

    private final RetrieveUserUseCase retrieveUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final ManageRoleUseCase manageRoleUseCase;

    public UserManagementService(
            @Qualifier("retrieveUserUseCaseImpl") RetrieveUserUseCase retrieveUserUseCase,
            @Qualifier("updateUserUseCaseImpl") UpdateUserUseCase updateUserUseCase,
            @Qualifier("manageRoleUseCaseImpl") ManageRoleUseCase manageRoleUseCase) {
        this.retrieveUserUseCase = retrieveUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.manageRoleUseCase = manageRoleUseCase;
    }

    @Override
    public Optional<User> findById(Long id) {
        return retrieveUserUseCase.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return retrieveUserUseCase.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return retrieveUserUseCase.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return retrieveUserUseCase.findAll();
    }

    @Override
    public boolean existsByUsername(String username) {
        return retrieveUserUseCase.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return retrieveUserUseCase.existsByEmail(email);
    }

    @Override
    public User update(User user) {
        return updateUserUseCase.update(user);
    }

    @Override
    public User setEnabled(Long userId, boolean enabled) {
        return updateUserUseCase.setEnabled(userId, enabled);
    }

    @Override
    public User assignRole(Long userId, String roleName) {
        return manageRoleUseCase.assignRole(userId, roleName);
    }

    @Override
    public User removeRole(Long userId, String roleName) {
        return manageRoleUseCase.removeRole(userId, roleName);
    }
}
