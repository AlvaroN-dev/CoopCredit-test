package com.riwi.microservice.auth.infrastructure.config;

import com.riwi.microservice.auth.application.usecases.*;
import com.riwi.microservice.auth.domain.port.in.*;
import com.riwi.microservice.auth.domain.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public AuthenticateUserUseCase authenticateUserUseCaseImpl(UserRepositoryPort userRepository, JwtTokenPort jwtTokenPort, PasswordEncoderPort passwordEncoder) {
        return new AuthenticateUserUseCaseImpl(userRepository, jwtTokenPort, passwordEncoder);
    }

    @Bean
    public RegisterUserUseCase registerUserUseCaseImpl(UserRepositoryPort userRepository, RoleRepositoryPort roleRepository, PasswordEncoderPort passwordEncoder) {
        return new RegisterUserUseCaseImpl(userRepository, roleRepository, passwordEncoder);
    }

    @Bean
    public ValidateTokenUseCase validateTokenUseCaseImpl(JwtTokenPort jwtTokenPort, UserRepositoryPort userRepository) {
        return new ValidateTokenUseCaseImpl(jwtTokenPort, userRepository);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCaseImpl(JwtTokenPort jwtTokenPort, UserRepositoryPort userRepository, ValidateTokenUseCase validateTokenUseCase) {
        return new RefreshTokenUseCaseImpl(jwtTokenPort, userRepository, validateTokenUseCase);
    }

    @Bean
    public RetrieveUserUseCase retrieveUserUseCaseImpl(UserRepositoryPort userRepository) {
        return new RetrieveUserUseCaseImpl(userRepository);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCaseImpl(UserRepositoryPort userRepository) {
        return new UpdateUserUseCaseImpl(userRepository);
    }

    @Bean
    public ManageRoleUseCase manageRoleUseCaseImpl(UserRepositoryPort userRepository, RoleRepositoryPort roleRepository) {
        return new ManageRoleUseCaseImpl(userRepository, roleRepository);
    }
}
