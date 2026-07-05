package com.comunetmax.ms_gateway.filter;

import com.comunetmax.ms_gateway.config.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            log.debug("Petición recibida en: {}", path);

            // --- ESTA ES LA ÚNICA ADICIÓN ---
            // Si es documentación, salta directamente al final (chain.filter)
            if (path.contains("/v3/api-docs") ||
                    path.contains("/swagger-ui") ||
                    path.contains("/swagger-resources") ||
                    path.contains("/webjars")) {
                return chain.filter(exchange);
            }

            // Endpoints públicos para consultar cobertura y planes sin autenticación
            if (isPublicPath(path)) {
                return chain.filter(exchange);
            }
            // --------------------------------

            // 2. AQUÍ EMPIEZA TU LÓGICA ORIGINAL (INTEGRA AL 100%)

            // ¿Tiene Header? (Tu validación original)
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Acceso no autorizado...", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // ¿Es Bearer? (Tu validación original)
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Acceso no autorizado...", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeader.substring(7);

            // ¿Es válido el Token? (Tu validación original con jwtUtil)
            if (!jwtUtil.validateToken(token)) {
                return onError(exchange, "Acceso no autorizado...", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private boolean isPublicPath(String path) {
        return path.equals("/ms-cobertura/api/cobertura") ||
               path.equals("/ms-cobertura/api/cobertura/") ||
               path.equals("/ms-cobertura/api/cobertura/municipios") ||
               path.startsWith("/ms-cobertura/api/cobertura/municipios/") ||
               path.startsWith("/ms-planes/api/planes/municipio/");
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // Cuerpo del JSON que verá el usuario en el navegador
        String body = "{\"error\": \"" + httpStatus.getReasonPhrase() +
                "\", \"mensaje\": \"" + message + "\"}";

        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {}


}