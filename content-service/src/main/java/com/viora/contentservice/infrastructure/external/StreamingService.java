package com.viora.contentservice.infrastructure.external;

import com.viora.contentservice.infrastructure.external.command.SaveMovieVideoCommand;

public interface StreamingService {

    void saveMovie(SaveMovieVideoCommand command);

}
