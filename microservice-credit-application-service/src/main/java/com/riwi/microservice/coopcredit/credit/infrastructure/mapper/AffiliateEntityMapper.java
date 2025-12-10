package com.riwi.microservice.coopcredit.credit.infrastructure.mapper;

import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.infrastructure.entities.AffiliateEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between Affiliate domain model and AffiliateEntity.
 */
@Component
public class AffiliateEntityMapper {

    /**
     * Maps AffiliateEntity to Affiliate domain model.
     */
    public Affiliate toDomain(AffiliateEntity entity) {
        if (entity == null) {
            return null;
        }

        Affiliate affiliate = new Affiliate();
        affiliate.setId(entity.getId());
        affiliate.setDocument(entity.getDocument());
        affiliate.setDocumentType(entity.getDocumentType());
        affiliate.setFirstName(entity.getFirstName());
        affiliate.setLastName(entity.getLastName());
        affiliate.setEmail(entity.getEmail());
        affiliate.setPhone(entity.getPhone());
        affiliate.setBirthDate(entity.getBirthDate());
        affiliate.setAddress(entity.getAddress());
        affiliate.setSalary(entity.getSalary());
        affiliate.setEmploymentStartDate(entity.getEmploymentStartDate());
        affiliate.setStatus(entity.getStatus());
        affiliate.setCreatedAt(entity.getCreatedAt());
        affiliate.setUpdatedAt(entity.getUpdatedAt());

        return affiliate;
    }

    /**
     * Maps Affiliate domain model to AffiliateEntity.
     */
    public AffiliateEntity toEntity(Affiliate domain) {
        if (domain == null) {
            return null;
        }

        AffiliateEntity entity = new AffiliateEntity();
        entity.setId(domain.getId());
        entity.setDocument(domain.getDocument());
        entity.setDocumentType(domain.getDocumentType());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setBirthDate(domain.getBirthDate());
        entity.setAddress(domain.getAddress());
        entity.setSalary(domain.getSalary());
        entity.setEmploymentStartDate(domain.getEmploymentStartDate());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    /**
     * Updates an existing entity from domain model.
     */
    public void updateEntityFromDomain(Affiliate domain, AffiliateEntity entity) {
        if (domain == null || entity == null) {
            return;
        }

        entity.setDocument(domain.getDocument());
        entity.setDocumentType(domain.getDocumentType());
        entity.setFirstName(domain.getFirstName());
        entity.setLastName(domain.getLastName());
        entity.setEmail(domain.getEmail());
        entity.setPhone(domain.getPhone());
        entity.setBirthDate(domain.getBirthDate());
        entity.setAddress(domain.getAddress());
        entity.setSalary(domain.getSalary());
        entity.setEmploymentStartDate(domain.getEmploymentStartDate());
        entity.setStatus(domain.getStatus());
        entity.setUpdatedAt(domain.getUpdatedAt());
    }
}
