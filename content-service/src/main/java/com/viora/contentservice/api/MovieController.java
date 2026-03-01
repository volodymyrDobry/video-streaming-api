package com.viora.contentservice.api;

import com.viora.contentservice.api.docs.MovieApi;
import com.viora.contentservice.api.request.AddMovieRequest;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.port.in.QueryMoviesUseCase;
import com.viora.contentservice.domain.vo.MovieSummary;
import com.viora.contentservice.infrastructure.service.ManageMoviesFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class MovieController implements MovieApi {

    private final ManageMoviesFacade manageMoviesFacade;
    private final QueryMoviesUseCase queryMoviesUseCase;

    public ResponseEntity<String> addMovie(AddMovieRequest details,
                                           MultipartFile file) {
        MovieDetails movie = manageMoviesFacade.addMovie(details, file);
        return ResponseEntity.created(URI.create("/api/v1/movies/%s".formatted(movie.getId())))
                .build();
    }

    public ResponseEntity<MovieDetails> getMovieById(Long id) {
        MovieDetails movie = queryMoviesUseCase.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    public ResponseEntity<Collection<MovieSummary>> getMoviesByName(String name) {
        Collection<MovieSummary> movies = queryMoviesUseCase.getMoviesByName(name);
        return ResponseEntity.ok(movies);
    }

}
