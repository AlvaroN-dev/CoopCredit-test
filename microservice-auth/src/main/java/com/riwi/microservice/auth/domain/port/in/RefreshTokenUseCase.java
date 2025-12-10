package com.riwi.microservice.auth.domain.port.in;

/**
 * Use case for token refresh.
 * Single Responsibility: Only handles token refresh operations.
 */
public interface RefreshTokenUseCase {
    
    /**
     * Refresh an existing token
     * @param token the current valid token
     * @return new AuthenticationResult with refreshed token
     */
    AuthenticateUserUseCase.AuthenticationResult refreshToken(String token);
}
