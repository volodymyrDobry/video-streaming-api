package com.viora.streamingandvideo.api.activemq.producer;

import com.viora.streamingandvideo.application.event.WatchMovieEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHistoryEventProducer {

    private final JmsTemplate jmsTemplate;

    @EventListener
    public void handleUserHistoryEvent(WatchMovieEvent event) {
        jmsTemplate.convertAndSend("user-movie-streaming-events.queue", event);
    }

}
