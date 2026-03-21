package com.viora.contentservice.infrastructure.external;

import com.viora.app.message.AddVideoPlayerMessage;
import com.viora.contentservice.domain.port.in.ManageMovieDetailsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StreamingAndVideoQueueListener {

    private final ManageMovieDetailsUseCase manageMovieDetailsUseCase;

    @JmsListener(destination = "movie-details.queue")
    public void receiveOrder(AddVideoPlayerMessage event) {
        log.info("Received message: {}", event);
        manageMovieDetailsUseCase.addMoviePlayer(event.imdbId(), event.playbackUrl());
    }
}
