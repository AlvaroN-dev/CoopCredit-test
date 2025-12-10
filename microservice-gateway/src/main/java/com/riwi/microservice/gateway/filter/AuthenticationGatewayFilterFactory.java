package com.riwi.microservice.gateway.filter;

import com.riwi.microservice.gateway.utils.JwtTokenProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Custom Gateway Filter Factory for role-based authentication.
 * Can be used in route configuration to require specific roles.
 */
@Component
public class AuthenticationGatewayFilterFactory 
        extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationGatewayFilterFactory(JwtTokenProvider jwtTokenProvider) {
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

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

            // Check required roles if configured
            if (config.getRequiredRoles() != null && !config.getRequiredRoles().isEmpty()) {
                if (!jwtTokenProvider.hasAnyRole(token, config.getRequiredRoles())) {
                    return onError(exchange, "Access denied. Required roles: " + config.getRequiredRoles(), 
                            HttpStatus.FORBIDDEN);
                }
            }

            // Add user info headers
            String username = jwtTokenProvider.getUsernameFromToken(token);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            List<String> roles = jwtTokenProvider.getRolesFromToken(token);

            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-User-Id", userId != null ? userId.toString() : "")
                    .header("X-User-Name", username)
                    .header("X-User-Roles", roles != null ? String.join(",", roles) : "")
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
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

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("requiredRoles");
    }

    /**
     * Configuration class for the filter.
     */
    public static class Config {
        private List<String> requiredRoles;

        public Config() {
        }

        public List<String> getRequiredRoles() {
            return requiredRoles;
        }

        public void setRequiredRoles(List<String> requiredRoles) {
            this.requiredRoles = requiredRoles;
        }
    }
}
