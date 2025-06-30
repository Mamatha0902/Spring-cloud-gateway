package com.example.gateway_application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;


@Configuration
public class GatewayConfig {
    private static final String MAX_AGE = "3600";

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders headers = exchange.getResponse().getHeaders();
                String origin = request.getHeaders().getOrigin();

                if (origin != null && !headers.containsKey(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)) {
                    headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
                }

                if (!headers.containsKey(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS)) {
                    headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
                }

                if (!headers.containsKey(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS)) {
                    headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization, *");
                }

                if (!headers.containsKey(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)) {
                    headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                }

                if (!headers.containsKey(HttpHeaders.ACCESS_CONTROL_MAX_AGE)) {
                    headers.set(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
                }

                // Short-circuit OPTIONS requests
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.OK);
                    return exchange.getResponse().setComplete();
                }
            }

            return chain.filter(exchange);
        };
    }
}
