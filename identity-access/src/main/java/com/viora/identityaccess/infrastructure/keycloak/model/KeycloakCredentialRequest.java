package com.viora.identityaccess.infrastructure.keycloak.model;

public record KeycloakCredentialRequest(String type, boolean temporary, String value) {}
