package com.viora.spi.keycloak;

import com.viora.spi.keycloak.handlers.KeycloakEventHandlersFactory;
import com.viora.spi.keycloak.handlers.KeycloakEventHandlingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;

@Slf4j
public class KeycloakEventListener implements EventListenerProvider {

    private final KeycloakSession keycloakSession;

    public KeycloakEventListener(KeycloakSession keycloakSession) {
        this.keycloakSession = keycloakSession;
    }

    @Override
    public void onEvent(Event event) {
        log.info("Received event: {} details: {}", event.getType(), event.getDetails());
        KeycloakEventHandlingStrategy strategy = KeycloakEventHandlersFactory.getStrategy(event.getType());
        strategy.handleEvent(event);
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {
        log.info("Received admin event, operation type {}, representation: {}", adminEvent.getOperationType(),
                adminEvent.getRepresentation());
    }

    @Override
    public void close() {
    }
}
