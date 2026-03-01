package com.viora.contentservice.domain.port.in;

import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.port.in.command.AddMovieCommand;

public interface ManageMovieDetailsUseCase {
    MovieDetails addMovieDetails(AddMovieCommand command);
}
