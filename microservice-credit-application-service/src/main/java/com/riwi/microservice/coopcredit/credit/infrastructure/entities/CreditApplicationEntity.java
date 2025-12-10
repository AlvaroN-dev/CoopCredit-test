package com.riwi.microservice.coopcredit.credit.infrastructure.entities;

import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA Entity for Credit Application.
 */
@Getter
@Setter

@Entity
@Table(name = "credit_applications", indexes = {
        @Index(name = "idx_credit_application_number", columnList = "application_number", unique = true),
        @Index(name = "idx_credit_application_status", columnList = "status"),
        @Index(name = "idx_credit_application_affiliate", columnList = "affiliate_id"),
        @Index(name = "idx_credit_application_date", columnList = "application_date")
})
@NamedEntityGraph(
        name = "CreditApplication.withDetails",
        attributeNodes = {
                @NamedAttributeNode("affiliate"),
                @NamedAttributeNode("riskEvaluation")
        }
)
public class CreditApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_number", nullable = false, unique = true, length = 20)
    private String applicationNumber;

    @Column(name = "requested_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal requestedAmount;

    @Column(name = "term_months", nullable = false)
    private Integer termMonths;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "purpose", nullable = false, length = 500)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CreditApplicationStatus status;

    @Column(name = "comments", length = 1000)
    private String comments;

    @Column(name = "application_date", nullable = false)
    private LocalDateTime applicationDate;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    @Column(name = "decision_date")
    private LocalDateTime decisionDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_id", nullable = false, foreignKey = @ForeignKey(name = "fk_credit_application_affiliate"))
    private AffiliateEntity affiliate;

    @OneToOne(mappedBy = "creditApplication", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RiskEvaluationEntity riskEvaluation;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (applicationDate == null) {
            applicationDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
}
