package org.example.userstreaminghistory.api.activemq.consumer;

import com.viora.app.message.UserHistoryFailedEvent;
import com.viora.app.message.UserHistorySaveEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userstreaminghistory.api.activemq.producer.UserHistoryFailuresProducer;
import org.example.userstreaminghistory.domain.ports.command.SaveUserHistoryCommand;
import org.example.userstreaminghistory.domain.ports.in.SaveUserHistoryUseCase;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoryEventsListener {

    private final SaveUserHistoryUseCase saveUserHistoryUseCase;
    private final UserHistoryFailuresProducer producer;

    @JmsListener(destination = "user-movie-streaming-events.queue")
    public void receiveOrder(UserHistorySaveEvent event) {
        log.info("Received message: {}", event);
        SaveUserHistoryCommand command = new SaveUserHistoryCommand(event.userId(), event.movieId(), event.segment());
        try {
            saveUserHistoryUseCase.saveUserHistory(command);
        } catch (Exception e) {
            String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            producer.handleSaveHistoryFailure(new UserHistoryFailedEvent(userId, event.movieId(), event.segment(), e.getMessage()));
            log.error("Error saving user history", e);
        }
    }

}
