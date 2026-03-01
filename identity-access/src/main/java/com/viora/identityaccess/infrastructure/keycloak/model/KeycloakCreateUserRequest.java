package com.viora.identityaccess.infrastructure.keycloak.model;

import lombok.Builder;

import java.util.Set;

@Builder
public record KeycloakCreateUserRequest(
        String username, String email, boolean enabled, Set<KeycloakCredentialRequest> credentials) {}
