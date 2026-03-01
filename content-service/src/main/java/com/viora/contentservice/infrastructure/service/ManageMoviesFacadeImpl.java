package com.viora.contentservice.infrastructure.service;

import com.viora.contentservice.api.request.AddMovieRequest;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.port.in.ManageMovieDetailsUseCase;
import com.viora.contentservice.domain.port.in.command.AddMovieCommand;
import com.viora.contentservice.infrastructure.external.StreamingService;
import com.viora.contentservice.infrastructure.external.command.SaveMovieVideoCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ManageMoviesFacadeImpl implements ManageMoviesFacade {

    private final ManageMovieDetailsUseCase manageMoviesUseCase;
    private final StreamingService streamingService;

    @Transactional
    @Override
    public MovieDetails addMovie(AddMovieRequest request, MultipartFile multipartFile) {
        if (Objects.isNull(request)) {
            return null;
        }
        AddMovieCommand addMovieCommand = mapToAddMovieCommand(request);
        MovieDetails addedMovie = manageMoviesUseCase.addMovieDetails(addMovieCommand);
        SaveMovieVideoCommand saveMovieVideoCommand =
                new SaveMovieVideoCommand(addedMovie.getId(), addedMovie.getName(), multipartFile);
        streamingService.saveMovie(saveMovieVideoCommand);
        return addedMovie;
    }

    private AddMovieCommand mapToAddMovieCommand(AddMovieRequest request) {
        return new AddMovieCommand(request.name(), request.plot(), request.actorsIds(), request.posterLink());
    }

}
