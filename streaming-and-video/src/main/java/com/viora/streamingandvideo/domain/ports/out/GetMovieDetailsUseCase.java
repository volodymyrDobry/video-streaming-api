package com.viora.streamingandvideo.domain.ports.out;

import com.viora.streamingandvideo.domain.model.MovieDetails;

public interface GetMovieDetailsUseCase {
    MovieDetails getMovieDetailsByImdbId(String imdbId);
}
