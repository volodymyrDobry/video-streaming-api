package com.viora.streamingandvideo.infrastructure.external;

import com.viora.streamingandvideo.infrastructure.external.exception.ContentTemporallyUnavailableException;
import com.viora.streamingandvideo.infrastructure.external.response.MovieDetailsResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieDetailsClient {

    private static final String MOVIE_DETAILS_PATH = "/api/v1/movies/movie?imdbId={imdbId}";

    @Qualifier("movieDetailsRestClient")
    private final RestClient restClient;

    @CircuitBreaker(name = "movie-details", fallbackMethod = "fallBack")
    @TimeLimiter(name = "movie-details", fallbackMethod = "fallBack")
    public CompletableFuture<MovieDetailsResponse> getMovieDetailsByImdbId(String imdbId) {
        return CompletableFuture.supplyAsync(() -> restClient.get()
                .uri(MOVIE_DETAILS_PATH, imdbId)
                .retrieve()
                .toEntity(MovieDetailsResponse.class).getBody());
    }

    public CompletableFuture<MovieDetailsResponse> fallBack(String imdbId, Throwable ex) {
        log.error("Failed to get movie by imdbId: {}", imdbId);
        throw new ContentTemporallyUnavailableException("Movie details service is temporarily unavailable");
    }
}
