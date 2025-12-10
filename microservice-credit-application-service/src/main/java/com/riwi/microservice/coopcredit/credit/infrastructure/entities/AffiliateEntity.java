package com.riwi.microservice.coopcredit.credit.infrastructure.entities;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity for Affiliate.
 */

@Getter
@Setter
@Entity
@Table(name = "affiliates", indexes = {
        @Index(name = "idx_affiliate_document", columnList = "document", unique = true),
        @Index(name = "idx_affiliate_email", columnList = "email"),
        @Index(name = "idx_affiliate_status", columnList = "status")
})
@NamedEntityGraph(
        name = "Affiliate.withCreditApplications",
        attributeNodes = @NamedAttributeNode("creditApplications")
)
public class AffiliateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document", nullable = false, unique = true, length = 20)
    private String document;

    @Column(name = "document_type", nullable = false, length = 20)
    private String documentType;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal salary;

    @Column(name = "employment_start_date")
    private LocalDate employmentStartDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AffiliateStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "affiliate", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.BatchSize(size = 10)
    private List<CreditApplicationEntity> creditApplications = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
}
