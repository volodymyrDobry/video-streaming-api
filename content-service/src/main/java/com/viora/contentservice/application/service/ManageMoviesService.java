package com.viora.contentservice.application.service;

import com.viora.contentservice.domain.domain.Actor;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.domain.Poster;
import com.viora.contentservice.domain.exception.ActorNotFoundException;
import com.viora.contentservice.domain.port.in.ManageMovieDetailsUseCase;
import com.viora.contentservice.domain.port.in.command.AddMovieCommand;
import com.viora.contentservice.domain.port.out.ActorsRepository;
import com.viora.contentservice.domain.port.out.MovieDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManageMoviesService implements ManageMovieDetailsUseCase {

    private final MovieDetailsRepository movieDetailsRepository;
    private final ActorsRepository actorsRepository;

    @Override
    public MovieDetails addMovieDetails(AddMovieCommand command) {
        Set<Actor> actors = mapStringToActors(command.actorsIds());
        Poster poster = new Poster(command.posterLink());

        MovieDetails movieDetails = MovieDetails.createMovieDetails(command.name(), command.plot(), actors, poster);
        movieDetails = movieDetailsRepository.saveMovieDetails(movieDetails);

        return movieDetails;
    }

    private Set<Actor> mapStringToActors(Set<Long> actorsIds) {
        Set<Actor> actors = actorsRepository.getActorsByIds(actorsIds);
        if (actors.size() != actorsIds.size()) {
            Set<Long> existingIds = actors.stream()
                    .map(Actor::getId)
                    .collect(Collectors.toSet());
            actorsIds.removeAll(existingIds);
            throw new ActorNotFoundException("Actors with ids %s weren't found".formatted(actorsIds));
        }

        return actors;
    }

}
