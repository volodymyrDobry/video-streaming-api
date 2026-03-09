package com.viora.contentservice.application.service;

import com.viora.contentservice.domain.domain.Actor;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.exception.MovieNotFoundException;
import com.viora.contentservice.domain.port.in.QueryActorsUseCase;
import com.viora.contentservice.domain.port.in.command.AddMovieCommand;
import com.viora.contentservice.domain.port.out.MovieDetailsRepository;
import com.viora.contentservice.domain.vo.MovieActor;
import com.viora.contentservice.domain.vo.Poster;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageMoviesServiceTest {

    @Mock
    private MovieDetailsRepository movieDetailsRepository;

    @Mock
    private QueryActorsUseCase queryActorsUseCase;

    @InjectMocks
    private ManageMoviesService service;

    @Test
    void addMovieDetailsSavesMappedMovieWithActorsAndPoster() {
        AddMovieCommand command = AddMovieCommand.builder()
                .name("Inception")
                .plot("Dreams")
                .actorsIds(Set.of("a1", "a2"))
                .posterLink("poster.png")
                .imdbId("tt1375666")
                .build();

        when(queryActorsUseCase.getActorsByIds(command.actorsIds()))
                .thenReturn(Set.of(new Actor("a1", "Actor One"), new Actor("a2", "Actor Two")));
        when(movieDetailsRepository.saveMovieDetails(any(MovieDetails.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MovieDetails result = service.addMovieDetails(command);

        ArgumentCaptor<MovieDetails> movieCaptor = ArgumentCaptor.forClass(MovieDetails.class);
        verify(movieDetailsRepository).saveMovieDetails(movieCaptor.capture());
        MovieDetails saved = movieCaptor.getValue();

        assertThat(result).isSameAs(saved);
        assertThat(saved.getName()).isEqualTo("Inception");
        assertThat(saved.getPlot()).isEqualTo("Dreams");
        assertThat(saved.getImdbId()).isEqualTo("tt1375666");
        assertThat(saved.getPoster()).isEqualTo(new Poster("poster.png"));
        assertThat(saved.getActors().stream().map(MovieActor::id).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder("a1", "a2");
    }

    @Test
    void addMoviePlayerPersistsUpdatedMovieWhenMovieExists() {
        MovieDetails movie = MovieDetails.createMovieDetails(
                "Inception",
                "Dreams",
                Set.of(new MovieActor("a1", "Actor One")),
                new Poster("poster.png"),
                "tt1375666"
        );
        when(movieDetailsRepository.getMovieDetailsById("10")).thenReturn(movie);

        service.addMoviePlayer("10", "hls/playlist.m3u8");

        assertThat(movie.getPlayer()).isNotNull();
        assertThat(movie.getPlayer().url()).isEqualTo("hls/playlist.m3u8");
        verify(movieDetailsRepository).saveMovieDetails(movie);
    }

    @Test
    void addMoviePlayerThrowsWhenMovieDoesNotExist() {
        when(movieDetailsRepository.getMovieDetailsById("77")).thenReturn(null);

        assertThatThrownBy(() -> service.addMoviePlayer("77", "hls/playlist.m3u8"))
                .isInstanceOf(MovieNotFoundException.class)
                .hasMessage("Movie with id 77 was not found");

        verify(movieDetailsRepository, never()).saveMovieDetails(any());
    }
}
