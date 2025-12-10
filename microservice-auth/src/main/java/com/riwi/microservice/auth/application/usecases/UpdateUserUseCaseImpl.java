package com.riwi.microservice.auth.application.usecases;

import com.riwi.microservice.auth.domain.exception.UserNotFoundException;
import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.UpdateUserUseCase;
import com.riwi.microservice.auth.domain.port.out.UserRepositoryPort;

import java.time.LocalDateTime;

public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final UserRepositoryPort userRepository;

    public UpdateUserUseCaseImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User update(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));
        
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(existingUser);
    }

    @Override
    public User setEnabled(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        
        user.setEnabled(enabled);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
}
