package com.viora.identityaccess.infrastructure.service;

import com.viora.identityaccess.domain.model.Role;
import com.viora.identityaccess.infrastructure.keycloak.model.KeycloakRole;

public enum ERole {

    USER {
        @Override
        public Role getPersistentValue() {
            return new Role(1L, "ROLE_USER");
        }

        @Override
        public KeycloakRole getKeycloakValue() {
            return new KeycloakRole("5a3fa96b-53b9-4440-99c8-86b73474698d", "ROLE_USER");
        }
    }, ADMIN {
        @Override
        public Role getPersistentValue() {
            return new Role(2L, "ROLE_ADMIN");
        }

        @Override
        public KeycloakRole getKeycloakValue() {
            return new KeycloakRole("e6e87add-46e1-4116-b08a-920cdcb96e9b", "ROLE_ADMIN");
        }
    };

    public abstract Role getPersistentValue();

    public abstract KeycloakRole getKeycloakValue();
}
