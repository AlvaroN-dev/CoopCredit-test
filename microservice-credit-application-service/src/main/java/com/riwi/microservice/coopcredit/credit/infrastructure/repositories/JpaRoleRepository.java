package com.riwi.microservice.coopcredit.credit.infrastructure.repositories;

import com.riwi.microservice.coopcredit.credit.infrastructure.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA Repository for RoleEntity.
 */
@Repository
public interface JpaRoleRepository extends JpaRepository<RoleEntity, Long> {
    
    Optional<RoleEntity> findByName(String name);
}
