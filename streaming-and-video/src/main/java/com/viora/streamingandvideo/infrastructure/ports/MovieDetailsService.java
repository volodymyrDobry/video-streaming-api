package com.viora.streamingandvideo.infrastructure.ports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viora.streamingandvideo.application.event.SaveMovieEvent;
import com.viora.streamingandvideo.domain.model.Movie;
import com.viora.streamingandvideo.domain.model.MovieDetails;
import com.viora.streamingandvideo.domain.ports.out.GetMovieDetailsUseCase;
import com.viora.streamingandvideo.infrastructure.external.MovieDetailsClient;
import com.viora.streamingandvideo.infrastructure.external.response.MovieDetailsResponse;
import com.viora.streamingandvideo.infrastructure.persistance.MovieTransactionRepository;
import com.viora.streamingandvideo.infrastructure.persistance.model.MovieTransaction;
import com.viora.streamingandvideo.infrastructure.persistance.model.MovieTransactionPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieDetailsService implements GetMovieDetailsUseCase {

    private final MovieDetailsClient movieDetailsClient;
    private final MovieTransactionRepository movieTransactionsRepository;

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
        MovieTransactionPayload movieTransactionPayload = new MovieTransactionPayload(event.getContentMovieId(), savedMoviePlayback.toString());
        MovieTransaction movieTransaction = MovieTransaction.createMovieTransaction(movieTransactionPayload);
        movieTransactionsRepository.save(movieTransaction);
    }

}
