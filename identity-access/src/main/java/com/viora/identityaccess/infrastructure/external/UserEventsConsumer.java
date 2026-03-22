package com.viora.identityaccess.infrastructure.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viora.identityaccess.api.request.KeycloakCreateAccountRequest;
import com.viora.identityaccess.infrastructure.service.AccountFacade;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventsConsumer {

    private final AccountFacade accountFacade;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = "users.queue")
    public void receiveOrder(TextMessage event) {
        log.info("Received message: {}", event);
        try {
            KeycloakCreateAccountRequest request = objectMapper.readValue(event.getText(), KeycloakCreateAccountRequest.class);
            accountFacade.createUserAccount(request);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message", e);
            throw new RuntimeException(e);
        } catch (JMSException e) {
            log.error("Failed to read message", e);
            throw new RuntimeException(e);
        }
    }


}
