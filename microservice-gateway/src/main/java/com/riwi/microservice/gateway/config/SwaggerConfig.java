package com.riwi.microservice.gateway.config;

import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration for Gateway.
 * Since Gateway is WebFlux-based, it cannot directly serve Swagger UI.
 * Swagger UI is served from individual microservices and routed through Gateway.
 */
@Configuration
public class SwaggerConfig {
    // No bean configuration needed
    // Swagger UI is accessed through routes to individual services
}
