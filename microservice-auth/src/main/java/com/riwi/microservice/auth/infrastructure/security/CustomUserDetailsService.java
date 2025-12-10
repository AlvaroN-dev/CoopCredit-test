package com.riwi.microservice.auth.infrastructure.security;

import com.riwi.microservice.auth.infrastructure.entities.UserEntity;
import com.riwi.microservice.auth.infrastructure.repositories.JpaUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of UserDetailsService for Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final JpaUserRepository userRepository;

    public CustomUserDetailsService(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<SimpleGrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                authorities
        );
    }
}
