package com.riwi.microservice.auth.application.usecases;

import com.riwi.microservice.auth.domain.exception.InvalidTokenException;
import com.riwi.microservice.auth.domain.exception.UserNotFoundException;
import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.AuthenticateUserUseCase.AuthenticationResult;
import com.riwi.microservice.auth.domain.port.in.RefreshTokenUseCase;
import com.riwi.microservice.auth.domain.port.in.ValidateTokenUseCase;
import com.riwi.microservice.auth.domain.port.out.JwtTokenPort;
import com.riwi.microservice.auth.domain.port.out.UserRepositoryPort;

public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    private final JwtTokenPort jwtTokenPort;
    private final UserRepositoryPort userRepository;
    private final ValidateTokenUseCase validateTokenUseCase;

    public RefreshTokenUseCaseImpl(JwtTokenPort jwtTokenPort, UserRepositoryPort userRepository, ValidateTokenUseCase validateTokenUseCase) {
        this.jwtTokenPort = jwtTokenPort;
        this.userRepository = userRepository;
        this.validateTokenUseCase = validateTokenUseCase;
    }

    @Override
    public AuthenticationResult refreshToken(String token) {
        if (!validateTokenUseCase.validateToken(token)) {
            throw new InvalidTokenException("Invalid token for refresh");
        }

        String username = jwtTokenPort.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String newToken = jwtTokenPort.generateToken(user);
        
        return new AuthenticationResult(newToken, jwtTokenPort.getExpirationTime(), user);
    }
}
