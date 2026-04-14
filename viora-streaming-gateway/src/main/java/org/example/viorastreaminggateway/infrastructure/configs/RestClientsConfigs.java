package org.example.viorastreaminggateway.infrastructure.configs;

import org.springframework.beans.factory.annotation.Value;
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

@Configuration
public class RestClientsConfigs {

    @Bean
    public RestClient userHistoryClient(
            @Value("${services.user-history.base-url:http://localhost:8085}") String userHistoryServiceBaseUrl
    ) {
        return RestClient.builder()
                .baseUrl(userHistoryServiceBaseUrl)
                .requestInterceptor(this::intercept)
                .build();
    }

    @Bean
    public RestClient contentClient(
            @Value("${services.content.base-url:http://localhost:8080}") String contentServiceBaseUrl
    ) {
        return RestClient.builder()
                .baseUrl(contentServiceBaseUrl)
                .requestInterceptor(this::intercept)
                .build();
    }

    private ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
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
