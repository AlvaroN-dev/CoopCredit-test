package com.riwi.microservice.auth.infrastructure.adapters;

import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.domain.port.out.UserRepositoryPort;
import com.riwi.microservice.auth.infrastructure.entities.UserEntity;
import com.riwi.microservice.auth.application.mapper.UserMapper;
import com.riwi.microservice.auth.infrastructure.repositories.JpaUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementing UserRepositoryPort using JPA.
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository, UserMapper userMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }
}
