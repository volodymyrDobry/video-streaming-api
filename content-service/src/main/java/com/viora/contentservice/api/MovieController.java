package com.viora.contentservice.api;

import com.viora.contentservice.api.docs.MovieApi;
import com.viora.contentservice.api.request.AddMovieRequest;
import com.viora.contentservice.api.request.AddPlayerRequest;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.port.in.ManageMovieDetailsUseCase;
import com.viora.contentservice.domain.port.in.QueryMoviesUseCase;
import com.viora.contentservice.domain.vo.MovieSummary;
import com.viora.contentservice.infrastructure.service.ManageMoviesFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class MovieController implements MovieApi {

    private final ManageMoviesFacade manageMoviesFacade;
    private final ManageMovieDetailsUseCase manageMovieDetailsUseCase;
    private final QueryMoviesUseCase queryMoviesUseCase;

    @Override
    public ResponseEntity<String> addMovie(AddMovieRequest details) {
        MovieDetails movie = manageMoviesFacade.addMovie(details);
        return ResponseEntity.created(URI.create("/api/v1/movies/%s".formatted(movie.getId())))
                .build();
    }

    @Override
    public ResponseEntity<MovieDetails> getMovieById(String id) {
        MovieDetails movie = queryMoviesUseCase.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    @Override
    public ResponseEntity<Collection<MovieSummary>> getMoviesByName(String name) {
        Collection<MovieSummary> movies = queryMoviesUseCase.getMoviesByName(name);
        return ResponseEntity.ok(movies);
    }

    @Override
    public ResponseEntity<MovieDetails> getMovieByImdbId(String imdbId) {
        MovieDetails movie = queryMoviesUseCase.getMovieByImdbId(imdbId);
        return ResponseEntity.ok(movie);
    }

    @Override
    public ResponseEntity<Void> setMoviePlayer(String movieId, AddPlayerRequest request) {
        manageMovieDetailsUseCase.addMoviePlayer(movieId, request.playerUrl());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Collection<MovieSummary>> getMoviesByImdbIds(Set<String> imdbIds) {
        return ResponseEntity.ok(queryMoviesUseCase.getMoviesByImdbIds(imdbIds));
    }

}
