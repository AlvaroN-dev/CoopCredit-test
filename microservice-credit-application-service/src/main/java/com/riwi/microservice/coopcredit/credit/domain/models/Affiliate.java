package com.riwi.microservice.coopcredit.credit.domain.models;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain model representing an Affiliate.
 * Contains business rules and invariants.
 */
public class Affiliate {
    
    private Long id;
    private String document;
    private String documentType;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String address;
    private BigDecimal salary;
    private LocalDate employmentStartDate;
    private AffiliateStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CreditApplication> creditApplications;

    public Affiliate() {
        this.creditApplications = new ArrayList<>();
    }

    public Affiliate(Long id, String document, String documentType, String firstName, 
                     String lastName, String email, String phone, LocalDate birthDate,
                     String address, BigDecimal salary, LocalDate employmentStartDate, AffiliateStatus status) {
        this.id = id;
        this.document = document;
        this.documentType = documentType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.salary = salary;
        this.employmentStartDate = employmentStartDate;
        this.status = status;
        this.creditApplications = new ArrayList<>();
    }

    // Business Rules

    /**
     * Validates that the affiliate can apply for credit.
     * Business Rule: Only ACTIVO affiliates can apply for credit.
     */
    public boolean canApplyForCredit() {
        return this.status == AffiliateStatus.ACTIVO;
    }

    /**
     * Validates that the salary is positive.
     * Business Rule: Salary must be greater than zero.
     */
    public boolean hasValidSalary() {
        return this.salary != null && this.salary.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Calculates the maximum credit amount based on salary.
     * Business Rule: Maximum credit is 6 times the salary.
     */
    public BigDecimal calculateMaxCreditAmount() {
        if (this.salary == null) {
            return BigDecimal.ZERO;
        }
        return this.salary.multiply(BigDecimal.valueOf(6));
    }

    /**
     * Gets the full name of the affiliate.
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Adds a credit application to the affiliate.
     */
    public void addCreditApplication(CreditApplication creditApplication) {
        this.creditApplications.add(creditApplication);
        creditApplication.setAffiliate(this);
    }

    /**
     * Gets the count of active credit applications.
     */
    public long getActiveCreditApplicationsCount() {
        return this.creditApplications.stream()
                .filter(app -> app.getStatus() == CreditApplicationStatus.PENDIENTE ||
                              app.getStatus() == CreditApplicationStatus.EN_REVISION ||
                              app.getStatus() == CreditApplicationStatus.APROBADA)
                .count();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public AffiliateStatus getStatus() {
        return status;
    }

    public void setStatus(AffiliateStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<CreditApplication> getCreditApplications() {
        return creditApplications;
    }

    public void setCreditApplications(List<CreditApplication> creditApplications) {
        this.creditApplications = creditApplications;
    }
}
