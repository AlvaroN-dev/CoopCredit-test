package com.riwi.microservice.auth.application.mapper;

import com.riwi.microservice.auth.domain.models.Role;
import com.riwi.microservice.auth.domain.models.User;
import com.riwi.microservice.auth.infrastructure.entities.RoleEntity;
import com.riwi.microservice.auth.infrastructure.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for converting between domain and entity objects.
 */
@Component
public class UserMapper {

    /**
     * Convert UserEntity to domain User
     */
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User();
        user.setId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEnabled(entity.isEnabled());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getRoles() != null) {
            Set<Role> roles = entity.getRoles().stream()
                    .map(this::toDomain)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        return user;
    }

    /**
     * Convert domain User to UserEntity
     */
    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEnabled(user.isEnabled());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());

        if (user.getRoles() != null) {
            Set<RoleEntity> roleEntities = user.getRoles().stream()
                    .map(this::toEntity)
                    .collect(Collectors.toSet());
            entity.setRoles(roleEntities);
        }

        return entity;
    }

    /**
     * Convert RoleEntity to domain Role
     */
    public Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }

        Role role = new Role();
        role.setId(entity.getId());
        role.setName(entity.getName());
        role.setDescription(entity.getDescription());
        role.setCreatedAt(entity.getCreatedAt());

        return role;
    }

    /**
     * Convert domain Role to RoleEntity
     */
    public RoleEntity toEntity(Role role) {
        if (role == null) {
            return null;
        }

        RoleEntity entity = new RoleEntity();
        entity.setId(role.getId());
        entity.setName(role.getName());
        entity.setDescription(role.getDescription());
        entity.setCreatedAt(role.getCreatedAt());

        return entity;
    }
}
