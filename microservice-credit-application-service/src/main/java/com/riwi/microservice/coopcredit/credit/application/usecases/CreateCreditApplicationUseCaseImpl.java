package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.AffiliateNotActiveException;
import com.riwi.microservice.coopcredit.credit.domain.exception.AffiliateNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskAssessmentResult;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.CreditApplicationStatus;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.RiskLevel;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateCreditApplicationUseCase;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.microservice.coopcredit.credit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskAssessmentPort;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskEvaluationRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CreateCreditApplicationUseCaseImpl implements CreateCreditApplicationUseCase {

    private final CreditApplicationRepositoryPort creditApplicationRepository;
    private final AffiliateRepositoryPort affiliateRepository;
    private final RiskAssessmentPort riskAssessmentPort;
    private final RiskEvaluationRepositoryPort riskEvaluationRepository;

    public CreateCreditApplicationUseCaseImpl(CreditApplicationRepositoryPort creditApplicationRepository,
                                              AffiliateRepositoryPort affiliateRepository,
                                              RiskAssessmentPort riskAssessmentPort,
                                              RiskEvaluationRepositoryPort riskEvaluationRepository) {
        this.creditApplicationRepository = creditApplicationRepository;
        this.affiliateRepository = affiliateRepository;
        this.riskAssessmentPort = riskAssessmentPort;
        this.riskEvaluationRepository = riskEvaluationRepository;
    }

    @Override
    @Transactional
    public CreditApplication createCreditApplication(CreateCreditApplicationCommand command) {
        // 1. Find affiliate
        Affiliate affiliate = affiliateRepository.findById(command.affiliateId())
                .orElseThrow(() -> new AffiliateNotFoundException(command.affiliateId()));

        if (!affiliate.canApplyForCredit()) {
            throw new AffiliateNotActiveException(command.affiliateId());
        }

        // 2. Register Request (PENDING)
        CreditApplication creditApplication = new CreditApplication();
        creditApplication.setApplicationNumber(creditApplicationRepository.generateApplicationNumber());
        creditApplication.setRequestedAmount(command.requestedAmount());
        creditApplication.setTermMonths(command.termMonths());
        creditApplication.setInterestRate(command.interestRate());
        creditApplication.setPurpose(command.purpose());
        creditApplication.setStatus(CreditApplicationStatus.PENDIENTE);
        creditApplication.setApplicationDate(LocalDateTime.now());
        creditApplication.setCreatedAt(LocalDateTime.now());
        creditApplication.setUpdatedAt(LocalDateTime.now());
        creditApplication.setAffiliate(affiliate);

        creditApplication = creditApplicationRepository.save(creditApplication);

        // 3. Invoke Risk Service
        RiskAssessmentResult riskResult = riskAssessmentPort.assessRisk(
                affiliate.getDocument(),
                command.requestedAmount().doubleValue(),
                command.termMonths()
        );

        // 4. Apply Policies
        boolean approved = true;
        StringBuilder rejectionReason = new StringBuilder();

        // Policy: Seniority (6 months)
        if (affiliate.getEmploymentStartDate() != null) {
            long monthsEmployed = ChronoUnit.MONTHS.between(affiliate.getEmploymentStartDate(), LocalDate.now());
            if (monthsEmployed < 6) {
                approved = false;
                rejectionReason.append("Antigüedad laboral insuficiente (< 6 meses). ");
            }
        } else {
             // If date is missing, we might reject or assume 0. Let's reject for safety.
             approved = false;
             rejectionReason.append("Fecha de inicio de empleo no registrada. ");
        }

        // Policy: Max Amount (e.g. 10x Salary)
        BigDecimal maxAmount = affiliate.getSalary().multiply(BigDecimal.valueOf(10));
        if (command.requestedAmount().compareTo(maxAmount) > 0) {
            approved = false;
            rejectionReason.append("Monto excede el límite permitido por salario. ");
        }

        // Policy: Debt-to-Income (Cuota / Ingreso < 30%)
        BigDecimal monthlyRate = command.interestRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal installment;
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            installment = command.requestedAmount().divide(BigDecimal.valueOf(command.termMonths()), 2, RoundingMode.HALF_UP);
        } else {
            // Formula: P * r / (1 - (1+r)^-n)
            // Using double for power calculation then back to BigDecimal
            double p = command.requestedAmount().doubleValue();
            double r = monthlyRate.doubleValue();
            int n = command.termMonths();
            double inst = (p * r) / (1 - Math.pow(1 + r, -n));
            installment = BigDecimal.valueOf(inst);
        }
        
        BigDecimal ratio = installment.divide(affiliate.getSalary(), 4, RoundingMode.HALF_UP);
        if (ratio.compareTo(new BigDecimal("0.30")) > 0) {
            approved = false;
            rejectionReason.append("Capacidad de endeudamiento insuficiente (Cuota/Ingreso > 30%). ");
        }

        // Policy: Risk Level
        RiskLevel riskLevel = parseRiskLevel(riskResult.riskLevel());
        if (riskLevel == RiskLevel.ALTO || riskLevel == RiskLevel.MUY_ALTO) {
            approved = false;
            rejectionReason.append("Nivel de riesgo alto según central de riesgos. ");
        }

        // 5. Generate RiskEvaluation
        RiskEvaluation evaluation = new RiskEvaluation();
        evaluation.setCreditScore(riskResult.score());
        evaluation.setRiskLevel(riskLevel);
        evaluation.setDebtToIncomeRatio(ratio);
        evaluation.setApproved(approved);
        evaluation.setRecommendation(approved ? "APROBADO" : "RECHAZADO: " + rejectionReason.toString());
        evaluation.setEvaluationDate(LocalDateTime.now());
        evaluation.setCreatedAt(LocalDateTime.now());
        evaluation.setUpdatedAt(LocalDateTime.now());
        evaluation.setCreditApplication(creditApplication);
        
        // Set missing required fields
        evaluation.setHasDefaultHistory(false); // Default assumption
        evaluation.setHasGuarantor(false); // Default assumption
        evaluation.setEvaluatedBy("SYSTEM");
        
        if (affiliate.getEmploymentStartDate() != null) {
            evaluation.setYearsEmployed((int) ChronoUnit.YEARS.between(affiliate.getEmploymentStartDate(), LocalDate.now()));
        } else {
            evaluation.setYearsEmployed(0);
        }
        
        riskEvaluationRepository.save(evaluation);

        // 6. Update Application
        if (approved) {
            creditApplication.setStatus(CreditApplicationStatus.APROBADA);
        } else {
            creditApplication.setStatus(CreditApplicationStatus.RECHAZADA);
        }
        
        return creditApplicationRepository.save(creditApplication);
    }

    private RiskLevel parseRiskLevel(String levelStr) {
        if (levelStr == null) return RiskLevel.ALTO;
        if (levelStr.contains("BAJO")) return RiskLevel.BAJO;
        if (levelStr.contains("MEDIO")) return RiskLevel.MEDIO;
        return RiskLevel.ALTO;
    }
}
