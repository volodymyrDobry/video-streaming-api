package com.viora.identityaccess.infrastructure.keycloak;

import com.viora.identityaccess.infrastructure.keycloak.model.KeycloakRole;
import com.viora.identityaccess.infrastructure.keycloak.request.CreateKeycloakAccountRequest;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public interface KeycloakClient {

    void deleteAccount(String id);

    void assignRoles(@NotEmpty String id, Set<KeycloakRole> keycloakRoles);

    String createAccount(CreateKeycloakAccountRequest keycloakAccountRequest);
}
