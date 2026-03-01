package com.viora.spi.keycloak.handlers;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.events.Event;

@Slf4j
public class DefaultKeycloakEventHandler implements KeycloakEventHandlingStrategy {
    @Override
    public void handleEvent(Event event) {
        log.info("Retrieved event {}", event);
    }
}
