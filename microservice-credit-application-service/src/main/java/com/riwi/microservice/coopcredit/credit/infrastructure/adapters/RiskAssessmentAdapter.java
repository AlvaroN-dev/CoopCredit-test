package com.riwi.microservice.coopcredit.credit.infrastructure.adapters;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskAssessmentResult;
import com.riwi.microservice.coopcredit.credit.domain.port.out.RiskAssessmentPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RiskAssessmentAdapter implements RiskAssessmentPort {

    private final RestTemplate restTemplate;

    public RiskAssessmentAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public RiskAssessmentResult assessRisk(String documento, Double monto, Integer plazo) {
        // Define request body structure locally or use a map
        record Request(String documento, Double monto, Integer plazo) {}
        record Response(String documento, Integer score, String nivelRiesgo, String detalle) {}

        String url = "http://microservice-risk-central-service/risk/evaluation";
        
        Response response = restTemplate.postForObject(
                url,
                new Request(documento, monto, plazo),
                Response.class
        );

        if (response == null) {
            throw new RuntimeException("Failed to get risk assessment");
        }

        return new RiskAssessmentResult(response.score(), response.nivelRiesgo(), response.detalle());
    }
}
