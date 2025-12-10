package com.riwi.microservice.auth.infrastructure.adapters;

import com.riwi.microservice.auth.domain.models.Role;
import com.riwi.microservice.auth.domain.port.out.RoleRepositoryPort;
import com.riwi.microservice.auth.infrastructure.entities.RoleEntity;
import com.riwi.microservice.auth.application.mapper.UserMapper;
import com.riwi.microservice.auth.infrastructure.repositories.JpaRoleRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter implementing RoleRepositoryPort using JPA.
 */
@Component
public class RoleRepositoryAdapter implements RoleRepositoryPort {

    private final JpaRoleRepository jpaRoleRepository;
    private final UserMapper userMapper;

    public RoleRepositoryAdapter(JpaRoleRepository jpaRoleRepository, UserMapper userMapper) {
        this.jpaRoleRepository = jpaRoleRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return jpaRoleRepository.findByName(name)
                .map(userMapper::toDomain);
    }

    @Override
    public Set<Role> findAll() {
        return jpaRoleRepository.findAll().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public Role save(Role role) {
        RoleEntity entity = userMapper.toEntity(role);
        RoleEntity savedEntity = jpaRoleRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }
}
