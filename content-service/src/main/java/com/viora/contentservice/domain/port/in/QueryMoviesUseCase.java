package com.viora.contentservice.domain.port.in;

import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.vo.MovieSummary;

import java.util.Set;

public interface QueryMoviesUseCase {
    MovieDetails getMovieById(String id);

    Set<MovieSummary> getMoviesByName(String summary);

    MovieDetails getMovieByImdbId(String imdbId);

    Set<MovieSummary> getMoviesByImdbIds(Set<String> imdbIds);
}
