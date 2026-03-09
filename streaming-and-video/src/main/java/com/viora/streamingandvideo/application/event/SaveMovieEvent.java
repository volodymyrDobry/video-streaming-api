package com.viora.streamingandvideo.application.event;

import com.viora.streamingandvideo.domain.model.Movie;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SaveMovieEvent extends ApplicationEvent {

    private final Movie movie;
    private final String contentMovieId;

    public SaveMovieEvent(Object source, Movie movie, String contentMovieId) {
        super(source);
        this.movie = movie;
        this.contentMovieId = contentMovieId;
    }
}
