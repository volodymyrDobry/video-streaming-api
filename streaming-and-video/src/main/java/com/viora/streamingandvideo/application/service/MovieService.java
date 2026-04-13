package com.viora.streamingandvideo.application.service;

import com.viora.streamingandvideo.application.event.SaveMovieEvent;
import com.viora.streamingandvideo.application.event.WatchMovieEvent;
import com.viora.streamingandvideo.domain.exception.MovieAlreadyExistsException;
import com.viora.streamingandvideo.domain.model.Movie;
import com.viora.streamingandvideo.domain.model.MovieDetails;
import com.viora.streamingandvideo.domain.ports.in.GetMovieUseCase;
import com.viora.streamingandvideo.domain.ports.in.SaveMovieUseCase;
import com.viora.streamingandvideo.domain.ports.in.command.SaveMovieCommand;
import com.viora.streamingandvideo.domain.ports.out.GetMovieDetailsUseCase;
import com.viora.streamingandvideo.domain.ports.out.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MovieService implements SaveMovieUseCase, GetMovieUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final MovieRepository movieRepository;
    private final GetMovieDetailsUseCase getMovieDetailsUseCase;

    @Override
    public Movie saveMovie(SaveMovieCommand command) {
        MovieDetails movieDetails = getMovieDetailsUseCase.getMovieDetailsByImdbId(command.imdbId());
        if (StringUtils.isNoneEmpty(movieDetails.playerUrl())) {
            throw new MovieAlreadyExistsException("Movie with imdbId " + command.imdbId() + " already exists");
        }
        Movie savedMovie = movieRepository.saveMovie(movieDetails, command.movie());
        applicationEventPublisher.publishEvent(new SaveMovieEvent(this, savedMovie, movieDetails.id()));

        return savedMovie;
    }

    @Override
    public Resource getMoviePlayback(String id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return movieRepository.getMoviePlayback(id);
    }

    @Override
    public Resource getMovieSegment(String id, Long segmentId) {
        if (id == null || segmentId == null) {
            throw new IllegalArgumentException("Neither id nor segmentId can be null");
        }
        applicationEventPublisher.publishEvent(new WatchMovieEvent(this, getUserId(), id, segmentId));
        return movieRepository.getMovieSegment(id, segmentId);
    }


    private String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
