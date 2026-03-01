package com.viora.spi.keycloak.handlers;

import com.viora.spi.usecase.RegisterAccountUseCase;
import com.viora.spi.usecase.command.RegisterUserCommand;
import lombok.RequiredArgsConstructor;
import org.keycloak.events.Event;

import java.util.Map;

@RequiredArgsConstructor
public class RegisterUserEventHandler implements KeycloakEventHandlingStrategy {

    private final RegisterAccountUseCase registerAccountUseCase;

    @Override
    public void handleEvent(Event event) {
        String email = event.getDetails().get("email");
        registerAccountUseCase.registerAccount(new RegisterUserCommand(event.getUserId(), email));
    }
}
