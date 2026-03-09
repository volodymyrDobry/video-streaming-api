package com.viora.contentservice.application.service;

import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.exception.DomainValidationException;
import com.viora.contentservice.domain.exception.MovieNotFoundException;
import com.viora.contentservice.domain.port.out.MovieDetailsRepository;
import com.viora.contentservice.domain.vo.MovieActor;
import com.viora.contentservice.domain.vo.MovieSummary;
import com.viora.contentservice.domain.vo.Poster;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryMoviesServiceTest {

    @Mock
    private MovieDetailsRepository movieRepository;

    @InjectMocks
    private QueryMoviesService service;

    @Test
    void getMovieByIdReturnsMovieWhenFound() {
        MovieDetails movie = MovieDetails.createMovieDetails(
                "Inception",
                "Dreams",
                Set.of(new MovieActor("a1", "Actor One")),
                new Poster("poster.png"),
                "tt1375666"
        );
        when(movieRepository.getMovieDetailsById("1")).thenReturn(movie);

        MovieDetails result = service.getMovieById("1");

        assertThat(result).isSameAs(movie);
    }

    @Test
    void getMovieByIdThrowsWhenMovieMissing() {
        when(movieRepository.getMovieDetailsById("1")).thenReturn(null);

        assertThatThrownBy(() -> service.getMovieById("1"))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessage("Movie with id 1 was not found");
    }

    @Test
    void getMoviesByNameUsesGetAllWhenNameIsNull() {
        Set<MovieSummary> movies = Set.of(new MovieSummary("m1", "Movie", new Poster("poster.png")));
        when(movieRepository.getAllMovies()).thenReturn(movies);

        Set<MovieSummary> result = service.getMoviesByName(null);

        assertThat(result).isEqualTo(movies);
        verify(movieRepository).getAllMovies();
    }

    @Test
    void getMoviesByNameUsesSearchWhenNameProvided() {
        Set<MovieSummary> movies = Set.of(new MovieSummary("m1", "Movie", new Poster("poster.png")));
        when(movieRepository.getMoviesSummariesByName("mov")).thenReturn(movies);

        Set<MovieSummary> result = service.getMoviesByName("mov");

        assertThat(result).isEqualTo(movies);
        verify(movieRepository).getMoviesSummariesByName("mov");
    }

    @Test
    void getMovieByImdbIdRejectsEmptyImdbId() {
        assertThatThrownBy(() -> service.getMovieByImdbId(""))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Movie Id cannot be empty");
    }

    @Test
    void getMovieByImdbIdThrowsWhenMovieMissing() {
        when(movieRepository.getMovieByImdbId("tt1375666")).thenReturn(null);

        assertThatThrownBy(() -> service.getMovieByImdbId("tt1375666"))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessage("Movie with imdbId tt1375666 was not found");
    }

    @Test
    void getMovieByImdbIdReturnsMovieWhenFound() {
        MovieDetails movie = MovieDetails.createMovieDetails(
                "Inception",
                "Dreams",
                Set.of(new MovieActor("a1", "Actor One")),
                new Poster("poster.png"),
                "tt1375666"
        );
        when(movieRepository.getMovieByImdbId("tt1375666")).thenReturn(movie);

        MovieDetails result = service.getMovieByImdbId("tt1375666");

        assertThat(result).isSameAs(movie);
    }
}
