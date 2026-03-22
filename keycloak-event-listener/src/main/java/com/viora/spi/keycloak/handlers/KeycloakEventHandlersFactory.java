package com.viora.spi.keycloak.handlers;


import com.viora.spi.activemq.UserMessageProducer;
import com.viora.spi.client.AccountsApiClient;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.events.EventType;

import java.util.Objects;

@Slf4j
public class KeycloakEventHandlersFactory {

    public static KeycloakEventHandlingStrategy getStrategy(EventType eventType) {
        if (Objects.requireNonNull(eventType) == EventType.REGISTER) {
            return new RegisterUserEventHandler(new UserMessageProducer());
        }
        return new DefaultKeycloakEventHandler();
    }


}
