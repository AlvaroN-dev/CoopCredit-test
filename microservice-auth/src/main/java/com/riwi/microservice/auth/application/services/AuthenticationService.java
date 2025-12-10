package com.riwi.microservice.auth.application.services;

import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.AuthenticateUserUseCase;
import com.riwi.microservice.auth.domain.port.in.RefreshTokenUseCase;
import com.riwi.microservice.auth.domain.port.in.RegisterUserUseCase;
import com.riwi.microservice.auth.domain.port.in.ValidateTokenUseCase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class AuthenticationService implements AuthenticateUserUseCase, RegisterUserUseCase, ValidateTokenUseCase, RefreshTokenUseCase {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthenticationService(
            @Qualifier("authenticateUserUseCaseImpl") AuthenticateUserUseCase authenticateUserUseCase,
            @Qualifier("registerUserUseCaseImpl") RegisterUserUseCase registerUserUseCase,
            @Qualifier("validateTokenUseCaseImpl") ValidateTokenUseCase validateTokenUseCase,
            @Qualifier("refreshTokenUseCaseImpl") RefreshTokenUseCase refreshTokenUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.registerUserUseCase = registerUserUseCase;
        this.validateTokenUseCase = validateTokenUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        return authenticateUserUseCase.authenticate(username, password);
    }

    @Override
    public User register(User user, String rawPassword, String roleName) {
        return registerUserUseCase.register(user, rawPassword, roleName);
    }

    @Override
    public boolean validateToken(String token) {
        return validateTokenUseCase.validateToken(token);
    }

    @Override
    public Optional<User> getUserFromToken(String token) {
        return validateTokenUseCase.getUserFromToken(token);
    }

    @Override
    public AuthenticationResult refreshToken(String token) {
        return refreshTokenUseCase.refreshToken(token);
    }
}
