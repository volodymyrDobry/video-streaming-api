package com.viora.streamingandvideo.domain.ports.out;

import com.viora.streamingandvideo.domain.model.Movie;
import org.springframework.core.io.Resource;

public interface MovieRepository {
    void saveMovie(Movie movie);

    Resource getMoviePlayback(String movieId);

    Resource getMovieSegment(String movieId, Long segmentId);
}
