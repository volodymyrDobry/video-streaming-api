package com.viora.streamingandvideo.application.service;

import com.viora.streamingandvideo.domain.model.Movie;
import com.viora.streamingandvideo.domain.ports.in.GetMovieUseCase;
import com.viora.streamingandvideo.domain.ports.in.SaveMovieUseCase;
import com.viora.streamingandvideo.domain.ports.in.command.SaveMovieCommand;
import com.viora.streamingandvideo.domain.ports.out.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MovieService implements SaveMovieUseCase, GetMovieUseCase {

    private final MovieRepository movieRepository;

    @Override
    public void saveMovie(SaveMovieCommand command) {
        Movie movie = Movie.createMovie(command.id(), command.name(), command.movie());
        movieRepository.saveMovie(movie);
    }

    @Override
    public Resource getMoviePlayback(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return movieRepository.getMoviePlayback(id.toString());
    }

    @Override
    public Resource getMovieSegment(Long id, Long segmentId) {
        if (id == null || segmentId == null) {
            throw new IllegalArgumentException("Neither id nor segmentId can be null");
        }
        return movieRepository.getMovieSegment(id.toString(), segmentId);
    }
}
