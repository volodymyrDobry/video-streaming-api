package com.viora.streamingandvideo.domain.ports.in;

import com.viora.streamingandvideo.domain.ports.in.command.SaveMovieCommand;

public interface SaveMovieUseCase {

    void saveMovie(SaveMovieCommand saveMovieCommand);

}
