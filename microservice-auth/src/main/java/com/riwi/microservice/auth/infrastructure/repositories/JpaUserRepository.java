package com.riwi.microservice.auth.infrastructure.repositories;

import com.riwi.microservice.auth.infrastructure.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for UserEntity.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByUsername(String username);
    
    Optional<UserEntity> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}
