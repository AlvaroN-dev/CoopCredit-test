package com.riwi.microservice.coopcredit.credit.application.usecases;

import com.riwi.microservice.coopcredit.credit.domain.exception.AffiliateNotActiveException;
import com.riwi.microservice.coopcredit.credit.domain.exception.AffiliateNotFoundException;
import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.CreditApplication;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskAssessmentResult;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskEvaluation;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.in.CreateCreditApplicationUseCase.CreateCreditApplicationCommand;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.microservice.coopcredit.credit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskAssessmentPort;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskEvaluationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCreditApplicationUseCaseTest {

    @Mock
    private CreditApplicationRepositoryPort creditApplicationRepository;
    @Mock
    private AffiliateRepositoryPort affiliateRepository;
    @Mock
    private RiskAssessmentPort riskAssessmentPort;
    @Mock
    private RiskEvaluationRepositoryPort riskEvaluationRepository;

    @InjectMocks
    private CreateCreditApplicationUseCaseImpl useCase;

    private Affiliate activeAffiliate;
    private CreateCreditApplicationCommand validCommand;

    @BeforeEach
    void setUp() {
        activeAffiliate = new Affiliate();
        activeAffiliate.setId(1L);
        activeAffiliate.setDocument("123456789");
        activeAffiliate.setStatus(AffiliateStatus.ACTIVO);
        activeAffiliate.setSalary(new BigDecimal("5000000"));
        activeAffiliate.setEmploymentStartDate(LocalDate.now().minusMonths(12)); // 1 year seniority

        validCommand = new CreateCreditApplicationCommand(
                1L,
                new BigDecimal("1000000"),
                12,
                new BigDecimal("1.5"),
                "Personal Loan"
        );
    }

    @Test
    void shouldCreateCreditApplicationSuccessfully() {
        // Arrange
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(activeAffiliate));
        when(creditApplicationRepository.generateApplicationNumber()).thenReturn("APP-001");
        when(creditApplicationRepository.save(any(CreditApplication.class))).thenAnswer(invocation -> {
            CreditApplication app = invocation.getArgument(0);
            app.setId(100L);
            return app;
        });
        
        RiskAssessmentResult riskResult = new RiskAssessmentResult(800, "BAJO RIESGO", "Good");
        when(riskAssessmentPort.assessRisk(anyString(), anyDouble(), anyInt())).thenReturn(riskResult);
        when(riskEvaluationRepository.save(any(RiskEvaluation.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CreditApplication result = useCase.createCreditApplication(validCommand);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("APP-001", result.getApplicationNumber());
        verify(affiliateRepository).findById(1L);
        verify(riskAssessmentPort).assessRisk("123456789", 1000000.0, 12);
        verify(creditApplicationRepository, atLeastOnce()).save(any(CreditApplication.class));
    }

    @Test
    void shouldThrowExceptionWhenAffiliateNotFound() {
        // Arrange
        when(affiliateRepository.findById(99L)).thenReturn(Optional.empty());
        CreateCreditApplicationCommand command = new CreateCreditApplicationCommand(
                99L, BigDecimal.TEN, 12, BigDecimal.ONE, "Test");

        // Act & Assert
        assertThrows(AffiliateNotFoundException.class, () -> useCase.createCreditApplication(command));
        verifyNoInteractions(riskAssessmentPort);
    }

    @Test
    void shouldThrowExceptionWhenAffiliateNotActive() {
        // Arrange
        activeAffiliate.setStatus(AffiliateStatus.INACTIVO);
        when(affiliateRepository.findById(1L)).thenReturn(Optional.of(activeAffiliate));

        // Act & Assert
        assertThrows(AffiliateNotActiveException.class, () -> useCase.createCreditApplication(validCommand));
        verifyNoInteractions(riskAssessmentPort);
    }
}
