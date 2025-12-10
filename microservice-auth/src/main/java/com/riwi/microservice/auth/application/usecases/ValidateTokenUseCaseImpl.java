package com.riwi.microservice.auth.application.usecases;

import com.riwi.microservice.auth.domain.exception.InvalidTokenException;
import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.ValidateTokenUseCase;
import com.riwi.microservice.auth.domain.port.out.JwtTokenPort;
import com.riwi.microservice.auth.domain.port.out.UserRepositoryPort;

import java.util.Optional;

public class ValidateTokenUseCaseImpl implements ValidateTokenUseCase {

    private final JwtTokenPort jwtTokenPort;
    private final UserRepositoryPort userRepository;

    public ValidateTokenUseCaseImpl(JwtTokenPort jwtTokenPort, UserRepositoryPort userRepository) {
        this.jwtTokenPort = jwtTokenPort;
        this.userRepository = userRepository;
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return jwtTokenPort.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<User> getUserFromToken(String token) {
        if (!validateToken(token)) {
            throw new InvalidTokenException("Invalid token");
        }
        
        String username = jwtTokenPort.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }
}
