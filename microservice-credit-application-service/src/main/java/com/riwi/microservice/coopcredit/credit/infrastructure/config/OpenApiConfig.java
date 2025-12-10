package com.riwi.microservice.coopcredit.credit.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for OpenAPI 3.0 documentation with JWT security.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Define security scheme for JWT
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization")
                .description("Ingresa el token JWT (sin el prefijo 'Bearer'). Ejemplo: eyJhbGciOiJIUzUxMiJ9...");

        // Security requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("CoopCredit API - Credit & Authentication Service")
                        .description("""
                                # API de Gestión de Créditos Cooperativos
                                
                                Esta API proporciona servicios para:
                                - 🔐 **Autenticación**: Registro y login de usuarios con JWT
                                - 👥 **Gestión de Afiliados**: CRUD de afiliados cooperativos
                                - 💳 **Solicitudes de Crédito**: Crear y gestionar aplicaciones de crédito
                                - ⚠️ **Evaluación de Riesgo**: Análisis automático de riesgo crediticio
                                
                                ## Autenticación
                                La mayoría de los endpoints requieren autenticación mediante token JWT.
                                
                                **Pasos para autenticarse:**
                                1. Registrar usuario: `POST /auth/register` (no requiere token)
                                2. Hacer login: `POST /auth/login` (devuelve el token JWT)
                                3. Usar el token en los demás endpoints haciendo clic en el botón **Authorize** 🔒
                                
                                ## Roles
                                - **AFILIADO**: Puede crear solicitudes para sí mismo
                                - **ANALISTA**: Puede gestionar solicitudes y aprobarlas/rechazarlas
                                - **ADMIN**: Acceso completo al sistema
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("CoopCredit Development Team")
                                .email("dev@coopcredit.com")
                                .url("https://coopcredit.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("API Gateway - Recommended (con prefijo /api)"),
                        new Server()
                                .url("http://localhost:8082")
                                .description("Credit Service - Direct Access (sin prefijo /api)")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
}
