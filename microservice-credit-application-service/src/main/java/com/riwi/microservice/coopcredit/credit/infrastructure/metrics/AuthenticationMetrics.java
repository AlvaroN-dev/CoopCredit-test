package com.riwi.microservice.coopcredit.credit.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * Servicio para registrar métricas relacionadas con autenticación
 * Permite rastrear:
 * - Intentos de login exitosos
 * - Intentos de login fallidos
 * - Registros de usuarios
 * - Validaciones de token
 */
@Component
public class AuthenticationMetrics {

    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;
    private final Counter registrationCounter;
    private final Counter tokenValidationSuccessCounter;
    private final Counter tokenValidationFailureCounter;
    private final Counter unauthorizedAccessCounter;

    public AuthenticationMetrics(MeterRegistry meterRegistry) {
        this.loginSuccessCounter = Counter.builder("auth.login.success")
                .description("Intentos de login exitosos")
                .register(meterRegistry);

        this.loginFailureCounter = Counter.builder("auth.login.failure")
                .description("Intentos de login fallidos")
                .tag("reason", "invalid_credentials")
                .register(meterRegistry);

        this.registrationCounter = Counter.builder("auth.registration")
                .description("Registros de usuarios")
                .register(meterRegistry);

        this.tokenValidationSuccessCounter = Counter.builder("auth.token.validation.success")
                .description("Validaciones de token exitosas")
                .register(meterRegistry);

        this.tokenValidationFailureCounter = Counter.builder("auth.token.validation.failure")
                .description("Validaciones de token fallidas")
                .register(meterRegistry);

        this.unauthorizedAccessCounter = Counter.builder("auth.unauthorized.access")
                .description("Intentos de acceso no autorizados")
                .register(meterRegistry);
    }

    public void recordLoginSuccess() {
        loginSuccessCounter.increment();
    }

    public void recordLoginFailure() {
        loginFailureCounter.increment();
    }

    public void recordRegistration() {
        registrationCounter.increment();
    }

    public void recordTokenValidationSuccess() {
        tokenValidationSuccessCounter.increment();
    }

    public void recordTokenValidationFailure() {
        tokenValidationFailureCounter.increment();
    }

    public void recordUnauthorizedAccess() {
        unauthorizedAccessCounter.increment();
    }
}
