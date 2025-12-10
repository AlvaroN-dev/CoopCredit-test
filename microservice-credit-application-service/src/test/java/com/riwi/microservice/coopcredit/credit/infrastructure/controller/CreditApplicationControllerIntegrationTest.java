package com.riwi.microservice.coopcredit.credit.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riwi.microservice.coopcredit.credit.AbstractIntegrationTest;
import com.riwi.microservice.coopcredit.credit.application.dto.credit.CreateCreditApplicationRequest;
import com.riwi.microservice.coopcredit.credit.domain.models.Affiliate;
import com.riwi.microservice.coopcredit.credit.domain.models.enums.AffiliateStatus;
import com.riwi.microservice.coopcredit.credit.domain.port.out.AffiliateRepositoryPort;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskAssessmentPort;
import com.riwi.microservice.coopcredit.credit.domain.models.RiskAssessmentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.transaction.annotation.Transactional;

import com.riwi.microservice.coopcredit.credit.infrastructure.repositories.JpaAffiliateRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CreditApplicationControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AffiliateRepositoryPort affiliateRepository;

    @Autowired
    private JpaAffiliateRepository jpaAffiliateRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RiskAssessmentPort riskAssessmentPort;

    private Affiliate testAffiliate;

    @BeforeEach
    void setUp() {
        jpaAffiliateRepository.deleteAll();

        // Setup mock for external risk service
        when(riskAssessmentPort.assessRisk(anyString(), anyDouble(), anyInt()))
                .thenReturn(new RiskAssessmentResult(800, "BAJO RIESGO", "Approved"));

        // Create a test affiliate in the DB
        testAffiliate = new Affiliate();
        testAffiliate.setDocument("999888777");
        testAffiliate.setDocumentType("CC");
        testAffiliate.setFirstName("Integration");
        testAffiliate.setLastName("Test");
        testAffiliate.setEmail("test@integration.com");
        testAffiliate.setPhone("3001234567");
        testAffiliate.setAddress("Test Address");
        testAffiliate.setBirthDate(LocalDate.of(1990, 1, 1));
        testAffiliate.setSalary(new BigDecimal("5000000"));
        testAffiliate.setStatus(AffiliateStatus.ACTIVO);
        testAffiliate.setEmploymentStartDate(LocalDate.now().minusMonths(24));
        
        testAffiliate = affiliateRepository.save(testAffiliate);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "ANALISTA"})
    void shouldCreateAndRetrieveCreditApplication() throws Exception {
        // 1. Create Request
        CreateCreditApplicationRequest request = new CreateCreditApplicationRequest();
        request.setAffiliateId(testAffiliate.getId());
        request.setRequestedAmount(new BigDecimal("2000000"));
        request.setTermMonths(12);
        request.setInterestRate(new BigDecimal("1.5"));
        request.setPurpose("Integration Test Loan");

        // 2. Perform POST
        String responseJson = mockMvc.perform(post("/credit/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.applicationNumber").exists())
                .andExpect(jsonPath("$.status").value("APROBADA"))
                .andReturn().getResponse().getContentAsString();
        
        // Extract ID from response
        Long createdId = objectMapper.readTree(responseJson).get("id").asLong();

        // 3. Perform GET
        mockMvc.perform(get("/credit/applications/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdId))
                .andExpect(jsonPath("$.requestedAmount").value(2000000));
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Role not allowed
    void shouldForbidAccessWithInsufficientRoles() throws Exception {
        mockMvc.perform(get("/credit/applications"))
                .andExpect(status().isForbidden());
    }
}
