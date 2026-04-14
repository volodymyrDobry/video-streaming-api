package com.viora.contentservice.application.service;

import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.exception.DomainValidationException;
import com.viora.contentservice.domain.exception.MovieNotFoundException;
import com.viora.contentservice.domain.port.in.QueryMoviesUseCase;
import com.viora.contentservice.domain.port.out.MovieDetailsRepository;
import com.viora.contentservice.domain.vo.MovieSummary;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QueryMoviesService implements QueryMoviesUseCase {

    private final MovieDetailsRepository movieRepository;

    @Override
    public MovieDetails getMovieById(String id) {
        var movie = movieRepository.getMovieDetailsById(id);
        if (movie == null) {
            throw new MovieNotFoundException("Movie with id %s was not found".formatted(id));
        }
        return movie;
    }

    @Override
    public Set<MovieSummary> getMoviesByName(String name) {
        if (name == null) {
            return movieRepository.getAllMovies();
        }
        return movieRepository.getMoviesSummariesByName(name);
    }

    @Override
    public MovieDetails getMovieByImdbId(String imdbId) {
        if (StringUtils.isEmpty(imdbId)) {
            throw new DomainValidationException("Movie Id cannot be empty");
        }
        return Optional.ofNullable(movieRepository.getMovieByImdbId(imdbId))
                .orElseThrow(() -> new MovieNotFoundException("Movie with imdbId %s was not found".formatted(imdbId)));
    }

    @Override
    public Set<MovieSummary> getMoviesByImdbIds(Set<String> imdbIds) {
        if (imdbIds != null && !imdbIds.isEmpty()) {
            return Set.of();
        }
        return movieRepository.getMoviesByImdbIds(imdbIds);
    }
}
