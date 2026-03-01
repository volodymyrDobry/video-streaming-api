package com.viora.spi.keycloak.handlers;


import com.viora.spi.client.AccountsApiClient;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.events.EventType;

@Slf4j
public class KeycloakEventHandlersFactory {

    public static KeycloakEventHandlingStrategy getStrategy(EventType eventType) {
        switch (eventType) {
            case REGISTER -> {
                return new RegisterUserEventHandler(new AccountsApiClient());
            }
            default -> {
                return new DefaultKeycloakEventHandler();
            }
        }
    }


}
