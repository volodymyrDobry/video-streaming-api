package org.example.viorastreaminggateway.infrastructure.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.List;

import static org.example.viorastreaminggateway.infrastructure.configs.security.JwtHelpersUtils.extractRoles;

@Slf4j
@Configuration
public class RestClientsConfigs {

    private final ServiceUrlsProperties serviceUrlsProperties;

    public RestClientsConfigs(ServiceUrlsProperties serviceUrlsProperties) {
        this.serviceUrlsProperties = serviceUrlsProperties;
    }

    @Bean
    @RefreshScope
    public RestClient userHistoryClient() {
        return RestClient.builder()
                .baseUrl(serviceUrlsProperties.getUserHistory().getBaseUrl())
                .requestInterceptor(this::intercept)
                .build();
    }

    @Bean
    @RefreshScope
    public RestClient contentClient() {
        return RestClient.builder()
                .baseUrl(serviceUrlsProperties.getContent().getBaseUrl())
                .requestInterceptor(this::intercept)
                .build();
    }

    private ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        log.info("Requesting: {}", SecurityContextHolder.getContext().getAuthentication().getClass());
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String userId = jwt.getSubject();
            String username = jwt.getClaimAsString("preferred_username");
            List<String> roles = extractRoles(jwt);

            request.getHeaders().add("X-User-Id", userId);
            request.getHeaders().add("X-User-Roles", String.join(",", roles));
            request.getHeaders().add("X-User-Username", username);
        }
        return execution.execute(request, body);
    }

}
