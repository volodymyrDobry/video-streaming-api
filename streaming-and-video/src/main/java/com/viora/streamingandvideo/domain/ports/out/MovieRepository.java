package com.viora.streamingandvideo.domain.ports.out;

import com.viora.streamingandvideo.domain.model.Movie;
import com.viora.streamingandvideo.domain.model.MovieDetails;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface MovieRepository {
    Movie saveMovie(MovieDetails movieDetails, MultipartFile movie);

    Resource getMoviePlayback(String movieId);

    Resource getMovieSegment(String movieId, Long segmentId);
}
