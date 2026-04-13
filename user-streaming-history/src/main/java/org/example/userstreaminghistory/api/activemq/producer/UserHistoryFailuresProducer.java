package org.example.userstreaminghistory.api.activemq.producer;

import lombok.RequiredArgsConstructor;
import org.example.userstreaminghistory.api.activemq.message.SaveHistoryEventFailure;
import org.example.userstreaminghistory.api.activemq.ports.UserHistoryEventHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHistoryFailuresProducer implements UserHistoryEventHandler {

    private final JmsTemplate jmsTemplate;

    @Override
    public void handleSaveHistoryFailure(SaveHistoryEventFailure failure) {
        jmsTemplate.convertAndSend("user-history-failures.queue", failure);
    }
}
