package com.riwi.microservice.coopcredit.credit.infrastructure.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de métricas personalizadas para observabilidad
 * Utiliza Micrometer para exponer métricas a Prometheus
 */
@Configuration
public class MetricsConfig {

    /**
     * Personaliza el MeterRegistry para agregar tags comunes a todas las métricas
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags(
                "service", "credit-application",
                "environment", System.getenv().getOrDefault("SPRING_PROFILES_ACTIVE", "dev")
        );
    }

    /**
     * Habilita el uso de la anotación @Timed para medir tiempos de ejecución
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
