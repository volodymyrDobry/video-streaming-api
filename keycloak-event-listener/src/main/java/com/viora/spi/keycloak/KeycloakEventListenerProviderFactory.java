package com.viora.spi.keycloak;

import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class KeycloakEventListenerProviderFactory implements EventListenerProviderFactory {

    private static final String LISTENER_ID = "event-listener-extension";

    @Override
    public EventListenerProvider create(KeycloakSession keycloakSession) {
        return new KeycloakEventListener(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return LISTENER_ID;
    }
}
