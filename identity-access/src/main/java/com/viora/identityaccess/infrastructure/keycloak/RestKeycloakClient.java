package com.viora.identityaccess.infrastructure.keycloak;

import com.viora.identityaccess.infrastructure.keycloak.model.KeycloakCreateUserRequest;
import com.viora.identityaccess.infrastructure.keycloak.model.KeycloakCredentialRequest;
import com.viora.identityaccess.infrastructure.keycloak.model.KeycloakRole;
import com.viora.identityaccess.infrastructure.keycloak.request.CreateKeycloakAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestKeycloakClient implements KeycloakClient {

    private static final String USER_BY_ID_PATH = "/admin/realms/viora-dev/users/{id}";
    private static final String USERS_PATH = "/admin/realms/viora-dev/users";
    private static final String ROLE_BY_USER_ID_PATH = "/admin/realms/viora-dev/users/{id}/role-mappings/realm";


    private final RestClient keycloakRestClient;

    @Override
    public void deleteAccount(String id) {
        keycloakRestClient.delete()
                .uri(USER_BY_ID_PATH, id)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void assignRoles(String id, Set<KeycloakRole> keycloakRoles) {
        keycloakRestClient
                .post()
                .uri(ROLE_BY_USER_ID_PATH, id)
                .body(keycloakRoles)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public String createAccount(CreateKeycloakAccountRequest keycloakAccountRequest) {
        final KeycloakCreateUserRequest keycloakCreateUserRequest =
                mapToKeycloakCreateUserRequest(keycloakAccountRequest);
        final ResponseEntity<Void> createdUserResponse =
                keycloakRestClient
                        .post()
                        .uri(USERS_PATH)
                        .body(keycloakCreateUserRequest)
                        .retrieve()
                        .toBodilessEntity();

        final String createUserId = extractCreatedUserIdFromLocation(createdUserResponse);
        assignRoles(createUserId, keycloakAccountRequest.roles());

        return createUserId;
    }

    private KeycloakCreateUserRequest mapToKeycloakCreateUserRequest(CreateKeycloakAccountRequest request) {
        return KeycloakCreateUserRequest.builder()
                .email(request.email())
                .enabled(true)
                .username(request.email())
                .credentials(Set.of(new KeycloakCredentialRequest("password", false, request.password())))
                .build();
    }

    private String extractCreatedUserIdFromLocation(final ResponseEntity<Void> response) {
        final String location = Objects.requireNonNull(response.getHeaders()
                        .get("location"))
                .get(0);
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
