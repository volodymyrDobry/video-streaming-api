package com.viora.streamingandvideo.application.service;

import com.viora.streamingandvideo.application.event.SaveMovieEvent;
import com.viora.streamingandvideo.domain.exception.MovieAlreadyExistsException;
import com.viora.streamingandvideo.domain.model.Movie;
import com.viora.streamingandvideo.domain.model.MovieDetails;
import com.viora.streamingandvideo.domain.ports.in.command.SaveMovieCommand;
import com.viora.streamingandvideo.domain.ports.out.GetMovieDetailsUseCase;
import com.viora.streamingandvideo.domain.ports.out.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GetMovieDetailsUseCase getMovieDetailsUseCase;

    @Mock
    private org.springframework.context.ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private MovieService service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "applicationEventPublisher", applicationEventPublisher);
    }

    @Test
    void saveMovieThrowsWhenMovieAlreadyHasPlayerUrl() {
        SaveMovieCommand command = new SaveMovieCommand(
                "tt1375666",
                new MockMultipartFile("movie", "movie.mp4", "video/mp4", "abc".getBytes())
        );
        when(getMovieDetailsUseCase.getMovieDetailsByImdbId("tt1375666"))
                .thenReturn(new MovieDetails("m1", "Inception", "tt1375666", "hls/playlist.m3u8"));

        assertThatThrownBy(() -> service.saveMovie(command))
                .isInstanceOf(MovieAlreadyExistsException.class)
                .hasMessage("Movie with imdbId tt1375666 already exists");

        verify(movieRepository, never()).saveMovie(any(), any());
        verify(applicationEventPublisher, never()).publishEvent(any());
    }

    @Test
    void saveMoviePersistsMovieAndPublishesEventWhenMovieIsNew() {
        SaveMovieCommand command = new SaveMovieCommand(
                "tt1375666",
                new MockMultipartFile("movie", "movie.mp4", "video/mp4", "abc".getBytes())
        );
        MovieDetails details = new MovieDetails("m1", "Inception", "tt1375666", null);
        Movie savedMovie = Movie.createMovie("tt1375666", new ByteArrayResource("video".getBytes()));

        when(getMovieDetailsUseCase.getMovieDetailsByImdbId("tt1375666")).thenReturn(details);
        when(movieRepository.saveMovie(details, command.movie())).thenReturn(savedMovie);

        Movie result = service.saveMovie(command);

        assertThat(result).isSameAs(savedMovie);
        ArgumentCaptor<SaveMovieEvent> eventCaptor = ArgumentCaptor.forClass(SaveMovieEvent.class);
        verify(applicationEventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getMovie()).isSameAs(savedMovie);
        assertThat(eventCaptor.getValue().getContentMovieId()).isEqualTo("m1");
    }

    @Test
    void getMoviePlaybackRejectsNullId() {
        assertThatThrownBy(() -> service.getMoviePlayback(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id cannot be null");
    }

    @Test
    void getMoviePlaybackDelegatesToRepository() {
        Resource resource = new ByteArrayResource("playback".getBytes());
        when(movieRepository.getMoviePlayback("tt1375666")).thenReturn(resource);

        Resource result = service.getMoviePlayback("tt1375666");

        assertThat(result).isSameAs(resource);
        verify(movieRepository).getMoviePlayback("tt1375666");
    }

    @Test
    void getMovieSegmentRejectsNullArguments() {
        assertThatThrownBy(() -> service.getMovieSegment(null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Neither id nor segmentId can be null");

        assertThatThrownBy(() -> service.getMovieSegment("tt1375666", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Neither id nor segmentId can be null");
    }

    @Test
    void getMovieSegmentDelegatesToRepository() {
        Resource resource = new ByteArrayResource("segment".getBytes());
        when(movieRepository.getMovieSegment("tt1375666", 3L)).thenReturn(resource);

        Resource result = service.getMovieSegment("tt1375666", 3L);

        assertThat(result).isSameAs(resource);
        verify(movieRepository).getMovieSegment("tt1375666", 3L);
    }
}
