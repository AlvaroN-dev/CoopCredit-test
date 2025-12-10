package com.riwi.microservice.auth.application.usecases;

import com.riwi.microservice.auth.domain.exception.AuthenticationException;
import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.in.AuthenticateUserUseCase;
import com.riwi.microservice.auth.domain.port.out.JwtTokenPort;
import com.riwi.microservice.auth.domain.port.out.PasswordEncoderPort;
import com.riwi.microservice.auth.domain.port.out.UserRepositoryPort;

public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final JwtTokenPort jwtTokenPort;
    private final PasswordEncoderPort passwordEncoder;

    public AuthenticateUserUseCaseImpl(UserRepositoryPort userRepository, JwtTokenPort jwtTokenPort, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenPort = jwtTokenPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        if (!user.isEnabled()) {
            throw new AuthenticationException("User account is disabled");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        String token = jwtTokenPort.generateToken(user);
        
        return new AuthenticationResult(token, jwtTokenPort.getExpirationTime(), user);
    }
}
