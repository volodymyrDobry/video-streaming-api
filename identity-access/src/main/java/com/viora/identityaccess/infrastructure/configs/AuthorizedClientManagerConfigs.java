package com.viora.identityaccess.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class AuthorizedClientManagerConfigs {

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            final OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
            final ClientRegistrationRepository clientRegistrationRepository) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository, oAuth2AuthorizedClientService);
    }

}
