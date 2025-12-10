package com.riwi.microservice.coopcredit.credit.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor para registrar métricas HTTP personalizadas
 * Mide:
 * - Tiempo de respuesta por endpoint
 * - Cantidad de requests por endpoint
 * - Errores por endpoint y código de estado
 */
@Component
public class HttpMetricsInterceptor implements HandlerInterceptor {

    private final MeterRegistry meterRegistry;
    private static final String TIMER_METRIC = "http.server.requests";
    private static final String ERROR_METRIC = "http.server.errors";
    private static final String REQUEST_COUNTER = "http.server.request.count";

    public HttpMetricsInterceptor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, 
                           @NonNull HttpServletResponse response, 
                           @NonNull Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, 
                               @NonNull HttpServletResponse response, 
                               @NonNull Object handler, 
                               Exception ex) {
        Long startTime = (Long) request.getAttribute("startTime");
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            String uri = request.getRequestURI();
            String method = request.getMethod();
            int status = response.getStatus();

            // Registrar tiempo de respuesta
            Timer.builder(TIMER_METRIC)
                    .tag("uri", uri)
                    .tag("method", method)
                    .tag("status", String.valueOf(status))
                    .tag("outcome", getOutcome(status))
                    .description("Tiempo de respuesta HTTP")
                    .register(meterRegistry)
                    .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);

            // Contador de requests
            Counter.builder(REQUEST_COUNTER)
                    .tag("uri", uri)
                    .tag("method", method)
                    .tag("status", String.valueOf(status))
                    .description("Total de solicitudes HTTP")
                    .register(meterRegistry)
                    .increment();

            // Registrar errores si el status es 4xx o 5xx
            if (status >= 400) {
                Counter.builder(ERROR_METRIC)
                        .tag("uri", uri)
                        .tag("method", method)
                        .tag("status", String.valueOf(status))
                        .tag("error_type", getErrorType(status))
                        .description("Errores HTTP por endpoint")
                        .register(meterRegistry)
                        .increment();
            }
        }
    }

    private String getOutcome(int status) {
        if (status >= 200 && status < 300) return "SUCCESS";
        if (status >= 300 && status < 400) return "REDIRECTION";
        if (status >= 400 && status < 500) return "CLIENT_ERROR";
        if (status >= 500) return "SERVER_ERROR";
        return "UNKNOWN";
    }

    private String getErrorType(int status) {
        if (status == 401 || status == 403) return "AUTHENTICATION_ERROR";
        if (status == 404) return "NOT_FOUND";
        if (status == 400) return "BAD_REQUEST";
        if (status == 409) return "CONFLICT";
        if (status >= 500) return "SERVER_ERROR";
        return "CLIENT_ERROR";
    }
}
