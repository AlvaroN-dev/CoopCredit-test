package com.riwi.microservice.gateway.filter;

import com.riwi.microservice.gateway.utils.JwtTokenProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Global filter for JWT authentication in the Gateway.
 * Validates JWT tokens and adds user information to request headers.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider jwtTokenProvider;
    private final RouteValidator routeValidator;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, RouteValidator routeValidator) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.routeValidator = routeValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Skip authentication for open endpoints
        if (routeValidator.isOpenEndpoint(request)) {
            return chain.filter(exchange);
        }

        // Check for Authorization header
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Invalid Authorization header format", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // Validate token
        if (!jwtTokenProvider.validateToken(token)) {
            return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        // Extract user info and add to headers for downstream services
        try {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId != null ? userId.toString() : "")
                    .header("X-User-Name", username)
                    .header("X-User-Roles", roles != null ? String.join(",", roles) : "")
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        } catch (Exception e) {
            return onError(exchange, "Error processing token: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public int getOrder() {
        return -100; // Run before other filters
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json");
        
        String body = String.format("{\"error\": \"%s\", \"message\": \"%s\", \"status\": %d}", 
                status.getReasonPhrase(), message, status.value());
        
        return response.writeWith(
                Mono.just(response.bufferFactory().wrap(body.getBytes()))
        );
    }
}
