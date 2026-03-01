package com.viora.identityaccess.infrastructure.keycloak.request;

import com.viora.identityaccess.infrastructure.keycloak.model.KeycloakRole;

import java.util.Set;

public record CreateKeycloakAccountRequest(
        String email,
        Set<KeycloakRole> roles,
        String password
) {
}
