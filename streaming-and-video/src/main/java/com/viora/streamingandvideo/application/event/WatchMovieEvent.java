package com.viora.streamingandvideo.application.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WatchMovieEvent extends ApplicationEvent {
    private final String userId;
    private final String movieId;
    private final Long segment;

    public WatchMovieEvent(Object source, String userId, String movieId, Long segment) {
        super(source);
        this.userId = userId;
        this.movieId = movieId;
        this.segment = segment;
    }
}
