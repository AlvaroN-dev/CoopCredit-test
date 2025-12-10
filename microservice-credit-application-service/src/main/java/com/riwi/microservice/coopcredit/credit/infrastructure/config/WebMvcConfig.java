package com.riwi.microservice.coopcredit.credit.infrastructure.config;

import com.riwi.microservice.coopcredit.credit.infrastructure.metrics.HttpMetricsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración Web MVC para registrar interceptores personalizados
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final HttpMetricsInterceptor httpMetricsInterceptor;

    public WebMvcConfig(HttpMetricsInterceptor httpMetricsInterceptor) {
        this.httpMetricsInterceptor = httpMetricsInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(httpMetricsInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/actuator/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                );
    }
}
