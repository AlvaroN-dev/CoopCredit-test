package com.riwi.microservice.coopcredit.credit.domain.port.out;

import com.riwi.microservice.coopcredit.credit.domain.models.RiskAssessmentResult;

public interface RiskAssessmentPort {
    RiskAssessmentResult assessRisk(String documento, Double monto, Integer plazo);
}
