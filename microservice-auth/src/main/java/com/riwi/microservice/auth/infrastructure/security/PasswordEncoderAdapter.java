package com.riwi.microservice.auth.infrastructure.security;

import com.riwi.microservice.auth.domain.port.out.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Adapter implementing PasswordEncoderPort using Spring Security's PasswordEncoder.
 */
@Component
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
