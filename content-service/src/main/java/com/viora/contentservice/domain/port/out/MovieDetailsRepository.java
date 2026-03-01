package com.viora.contentservice.domain.port.out;

import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.vo.MovieSummary;

import java.util.Set;

public interface MovieDetailsRepository {
    MovieDetails saveMovieDetails(MovieDetails movieDetails);

    Set<MovieSummary> getMoviesSummariesByName(String name);

    MovieDetails getMovieDetailsById(Long id);

    Set<MovieSummary> getAllMovies();
}
