package com.viora.contentservice.application.service;

import com.viora.contentservice.domain.exception.MovieNotFoundException;
import com.viora.contentservice.domain.vo.MovieActor;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.vo.Player;
import com.viora.contentservice.domain.vo.Poster;
import com.viora.contentservice.domain.port.in.ManageMovieDetailsUseCase;
import com.viora.contentservice.domain.port.in.QueryActorsUseCase;
import com.viora.contentservice.domain.port.in.command.AddMovieCommand;
import com.viora.contentservice.domain.port.out.MovieDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManageMoviesService implements ManageMovieDetailsUseCase {

    private final MovieDetailsRepository movieDetailsRepository;
    private final QueryActorsUseCase getActorsUseCase;

    @Override
    public MovieDetails addMovieDetails(AddMovieCommand command) {
        Set<MovieActor> actors = getActorsUseCase.getActorsByIds(command.actorsIds())
                .stream()
                .map(actor -> new MovieActor(actor.getId(), actor.getName()))
                .collect(Collectors.toSet());
        Poster poster = new Poster(command.posterLink());

        MovieDetails movieDetails = MovieDetails.createMovieDetails(command.name(), command.plot(), actors, poster, command.imdbId());
        movieDetails = movieDetailsRepository.saveMovieDetails(movieDetails);

        return movieDetails;
    }

    @Override
    public void addMoviePlayer(String movieId, String movieUrl) {
        MovieDetails movieDetails = Optional.ofNullable(movieDetailsRepository.getMovieDetailsById(movieId))
                .orElseThrow(() -> new MovieNotFoundException("Movie with id %s was not found".formatted(movieId)));
        Player player = new Player(movieUrl);
        movieDetails.setPlayer(player);
        movieDetailsRepository.saveMovieDetails(movieDetails);
    }

}
