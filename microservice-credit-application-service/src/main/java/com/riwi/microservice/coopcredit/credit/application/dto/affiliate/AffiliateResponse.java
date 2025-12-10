package com.riwi.microservice.coopcredit.credit.application.dto.affiliate;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for Affiliate.
 */
@Schema(description = "Response DTO for Affiliate")
public class AffiliateResponse {
    
    @Schema(description = "Unique ID of the affiliate", example = "1")
    private Long id;
    @Schema(description = "Document number", example = "123456789")
    private String document;
    @Schema(description = "Document type", example = "CC")
    private String documentType;
    @Schema(description = "First name", example = "John")
    private String firstName;
    @Schema(description = "Last name", example = "Doe")
    private String lastName;
    @Schema(description = "Full name", example = "John Doe")
    private String fullName;
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;
    @Schema(description = "Phone number", example = "+57 300 123 4567")
    private String phone;
    @Schema(description = "Birth date", example = "1990-01-01")
    private LocalDate birthDate;
    @Schema(description = "Address", example = "Calle 123 # 45-67")
    private String address;
    @Schema(description = "Monthly salary", example = "2500000")
    private BigDecimal salary;
    @Schema(description = "Maximum credit amount allowed", example = "50000000")
    private BigDecimal maxCreditAmount;
    @Schema(description = "Status of the affiliate", example = "ACTIVE")
    private AffiliateStatus status;
    @Schema(description = "Record creation timestamp")
    private LocalDateTime createdAt;
    @Schema(description = "Record update timestamp")
    private LocalDateTime updatedAt;

    public AffiliateResponse() {}

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

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

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

    public BigDecimal getMaxCreditAmount() { return maxCreditAmount; }
    public void setMaxCreditAmount(BigDecimal maxCreditAmount) { this.maxCreditAmount = maxCreditAmount; }

    public AffiliateStatus getStatus() { return status; }
    public void setStatus(AffiliateStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
