package com.viora.identityaccess.infrastructure.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Slf4j
@Configuration
public class RestClientConfigs {

    @Value("${spring.application.keycloak.url}")
    private String keycloakUrl;

    @Bean("keycloak")
    public RestClient keycloakRestClient(
            final RestClient.Builder builder,
            final OAuth2AuthorizedClientManager authorizedClientManager) {
        final OAuth2ClientHttpRequestInterceptor requestInterceptor =
                new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return builder
                .baseUrl(keycloakUrl)
                .requestInterceptor(
                        ((request, body, execution) -> {
                            final Consumer<Map<String, Object>> consumer =
                                    clientRegistrationId("user-management-client");
                            consumer.accept(request.getAttributes());

                            return execution.execute(request, body);
                        }))
                .requestInterceptor(requestInterceptor)
                .build();
    }


}
