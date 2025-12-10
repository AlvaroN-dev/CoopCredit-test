package com.riwi.microservice.coopcredit.credit.infrastructure.entities;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity for Affiliate.
 */
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

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public LocalDate getEmploymentStartDate() { return employmentStartDate; }
    public void setEmploymentStartDate(LocalDate employmentStartDate) { this.employmentStartDate = employmentStartDate; }

    public AffiliateStatus getStatus() { return status; }
    public void setStatus(AffiliateStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<CreditApplicationEntity> getCreditApplications() { return creditApplications; }
    public void setCreditApplications(List<CreditApplicationEntity> creditApplications) { this.creditApplications = creditApplications; }
}
