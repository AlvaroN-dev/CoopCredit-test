package com.riwi.microservice.auth.application.usecases;

import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.RetrieveUserUseCase;
import com.riwi.microservice.auth.domain.port.out.UserRepositoryPort;

import java.util.List;
import java.util.Optional;

public class RetrieveUserUseCaseImpl implements RetrieveUserUseCase {

    private final UserRepositoryPort userRepository;

    public RetrieveUserUseCaseImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
