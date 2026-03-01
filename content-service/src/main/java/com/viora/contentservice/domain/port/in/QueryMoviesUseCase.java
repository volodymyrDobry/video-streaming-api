package com.viora.contentservice.domain.port.in;

import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.vo.MovieSummary;

import java.util.Set;

public interface QueryMoviesUseCase {
    MovieDetails getMovieById(Long id);

    Set<MovieSummary> getMoviesByName(String summary);
}
