package com.riwi.microservice.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

/**
 * Validates routes to determine which require authentication.
 */
@Component
public class RouteValidator {

    /**
     * List of open endpoints that don't require authentication.
     */
    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/validate",
            "/auth/login",
            "/auth/register",
            "/auth/validate",
            "/actuator",
            "/actuator/health",
            "/actuator/info",
            "/swagger-ui",
            "/v3/api-docs",
            "/webjars"
    );

    /**
     * Predicate to check if a request is for a secured endpoint.
     */
    public Predicate<ServerHttpRequest> isSecured = request ->
            OPEN_ENDPOINTS.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

    /**
     * Check if the request path is an open (non-secured) endpoint.
     * @param request the HTTP request
     * @return true if the endpoint is open and doesn't require authentication
     */
    public boolean isOpenEndpoint(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return OPEN_ENDPOINTS.stream()
                .anyMatch(path::contains);
    }

    /**
     * Check if the request requires authentication.
     * @param request the HTTP request
     * @return true if authentication is required
     */
    public boolean requiresAuthentication(ServerHttpRequest request) {
        return !isOpenEndpoint(request);
    }
}
