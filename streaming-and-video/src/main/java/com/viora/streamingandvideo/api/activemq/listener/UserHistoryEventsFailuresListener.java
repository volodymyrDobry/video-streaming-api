package com.viora.streamingandvideo.api.activemq.listener;


import com.viora.app.message.UserHistoryFailedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserHistoryEventsFailuresListener {

    @JmsListener(destination = "user-history-failures.queue")
    public void handleFailure(UserHistoryFailedEvent failedEvent) {
        log.error("User history failed event: {}", failedEvent);
    }

}
