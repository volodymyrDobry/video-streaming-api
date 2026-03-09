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
            return new KeycloakRole("280ddc2b-e895-419f-b8d2-cae23d4f2ce8", "ROLE_USER");
        }
    }, ADMIN {
        @Override
        public Role getPersistentValue() {
            return new Role(2L, "ROLE_ADMIN");
        }

        @Override
        public KeycloakRole getKeycloakValue() {
            return new KeycloakRole("76d3449a-6fd9-4ac2-ab05-266703fc06a4", "ROLE_ADMIN");
        }
    };

    public abstract Role getPersistentValue();

    public abstract KeycloakRole getKeycloakValue();
}
