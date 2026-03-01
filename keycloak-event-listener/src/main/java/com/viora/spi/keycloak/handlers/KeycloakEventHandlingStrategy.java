package com.viora.spi.keycloak.handlers;

import org.keycloak.events.Event;

public interface KeycloakEventHandlingStrategy {

    void handleEvent(Event event);

}
