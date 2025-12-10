package com.riwi.microservice.auth.domain.port.in;

import com.riwi.microservice.auth.domain.models.User;

/**
 * Use case for user authentication.
 * Single Responsibility: Only handles authentication logic.
 */
public interface AuthenticateUserUseCase {
    
    /**
     * Authenticate a user with username and password
     * @param username the username
     * @param password the raw password
     * @return AuthenticationResult containing token and user info
     */
    AuthenticationResult authenticate(String username, String password);
    
    /**
     * Result class for authentication operations
     */
    record AuthenticationResult(
            String accessToken,
            String tokenType,
            Long expiresIn,
            User user
    ) {
        public AuthenticationResult(String accessToken, Long expiresIn, User user) {
            this(accessToken, "Bearer", expiresIn, user);
        }
    }
}
