package com.viora.streamingandvideo.domain.ports.in;

import com.viora.streamingandvideo.domain.model.Movie;
import com.viora.streamingandvideo.domain.ports.in.command.SaveMovieCommand;

public interface SaveMovieUseCase {

    Movie saveMovie(SaveMovieCommand saveMovieCommand);

}
