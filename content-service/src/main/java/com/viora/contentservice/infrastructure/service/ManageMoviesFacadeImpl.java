package com.viora.contentservice.infrastructure.service;

import com.viora.contentservice.api.request.AddMovieRequest;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.port.in.ManageMovieDetailsUseCase;
import com.viora.contentservice.domain.port.in.command.AddMovieCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ManageMoviesFacadeImpl implements ManageMoviesFacade {

    private final ManageMovieDetailsUseCase manageMoviesUseCase;

    @Override
    public MovieDetails addMovie(AddMovieRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        AddMovieCommand addMovieCommand = mapToAddMovieCommand(request);
        return manageMoviesUseCase.addMovieDetails(addMovieCommand);
    }

    private AddMovieCommand mapToAddMovieCommand(AddMovieRequest request) {
        return new AddMovieCommand(request.name(), request.plot(), request.actorsIds(), request.posterLink(), request.imdbId());
    }

}
