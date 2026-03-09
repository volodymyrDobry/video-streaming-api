package com.viora.streamingandvideo.infrastructure.ports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viora.streamingandvideo.application.event.SaveMovieEvent;
import com.viora.streamingandvideo.domain.model.Movie;
import com.viora.streamingandvideo.domain.model.MovieDetails;
import com.viora.streamingandvideo.domain.ports.out.GetMovieDetailsUseCase;
import com.viora.streamingandvideo.infrastructure.external.MovieDetailsClient;
import com.viora.streamingandvideo.infrastructure.external.response.MovieDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class MovieDetailsService implements GetMovieDetailsUseCase {

    private final MovieDetailsClient movieDetailsClient;

    private final ObjectMapper objectMapper;

    @Override
    public MovieDetails getMovieDetailsByImdbId(String imdbId) {
        MovieDetailsResponse response = movieDetailsClient.getMovieDetailsByImdbId(imdbId).join();
        return objectMapper.convertValue(response, MovieDetails.class);
    }


    @EventListener(SaveMovieEvent.class)
    public void saveMovie(SaveMovieEvent event) {
        Movie movie = event.getMovie();
        URI savedMoviePlayback = URI.create("/api/v1/streaming/movies/%s/index.m3u8".formatted(movie.getId()));
        movieDetailsClient.saveMovie(movie.getId(), savedMoviePlayback);
    }

}
