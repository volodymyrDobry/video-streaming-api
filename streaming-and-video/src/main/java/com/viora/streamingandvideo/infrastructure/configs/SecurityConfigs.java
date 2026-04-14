package com.viora.streamingandvideo.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class SecurityConfigs {

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity httpSecurity, GatewayHeadersFilter gatewayHeadersFilter) throws
            Exception {

        httpSecurity.oauth2ResourceServer(configurer -> {
            configurer.jwt(Customizer.withDefaults());
        });

        httpSecurity.authorizeHttpRequests(requestRegistry -> {
            requestRegistry.requestMatchers("/swagger-ui/*", "/v3/api-docs", "/v3/api-docs.yaml", "/v3/api-docs/*")
                    .permitAll();
            requestRegistry.requestMatchers(HttpMethod.OPTIONS)
                    .permitAll();
            requestRegistry.requestMatchers("/actuator/health")
                    .permitAll();
            requestRegistry.anyRequest()
                    .authenticated();
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.addFilterBefore(gatewayHeadersFilter, AnonymousAuthenticationFilter.class);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.logout(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            final OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
            final ClientRegistrationRepository clientRegistrationRepository) {
        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);
        var provider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
        manager.setAuthorizedClientProvider(provider);
        return manager;
    }

}
