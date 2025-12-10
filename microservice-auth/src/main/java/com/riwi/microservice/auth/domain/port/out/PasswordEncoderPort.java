package com.riwi.microservice.auth.domain.port.out;

/**
 * Output port for password encoding operations.
 */
public interface PasswordEncoderPort {
    
    /**
     * Encode a raw password
     * @param rawPassword the raw password
     * @return the encoded password
     */
    String encode(String rawPassword);
    
    /**
     * Check if a raw password matches an encoded password
     * @param rawPassword the raw password
     * @param encodedPassword the encoded password
     * @return true if matches
     */
    boolean matches(String rawPassword, String encodedPassword);
}
