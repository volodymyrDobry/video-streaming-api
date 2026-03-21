package com.viora.contentservice.infrastructure.persistance.adapter;

import com.viora.contentservice.domain.vo.MovieActor;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.vo.Poster;
import com.viora.contentservice.domain.port.out.MovieDetailsRepository;
import com.viora.contentservice.domain.vo.MovieSummary;
import com.viora.contentservice.infrastructure.persistance.model.MovieActorModel;
import com.viora.contentservice.infrastructure.persistance.model.MovieModel;
import com.viora.contentservice.infrastructure.persistance.repository.MongoMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaMovieRepositoryAdapter implements MovieDetailsRepository {

    private final MongoMovieRepository jpaMovieRepository;

    @Override
    public MovieDetails saveMovieDetails(MovieDetails movie) {
        MovieModel movieModel = mapToMovieModel(movie);
        movieModel = jpaMovieRepository.save(movieModel);
        return mapToMovie(movieModel);
    }

    @Override
    public Set<MovieSummary> getMoviesSummariesByName(String name) {
        return jpaMovieRepository.findAllByNameContaining(name)
                .stream()
                .map(m -> new MovieSummary(m.getId(), m.getName(), new Poster(m.getPosterLink())))
                .collect(Collectors.toSet());
    }

    @Override
    public MovieDetails getMovieDetailsById(String id) {
        Optional<MovieModel> movie = jpaMovieRepository.getMovieModelById(id);
        return movie.map(this::mapToMovie)
                .orElse(null);
    }

    @Override
    public Set<MovieSummary> getAllMovies() {
        return jpaMovieRepository.findAll()
                .stream()
                .map(m -> new MovieSummary(m.getId(), m.getName(), new Poster(m.getPosterLink())))
                .collect(Collectors.toSet());
    }

    @Override
    public MovieDetails getMovieByImdbId(String imdbId) {
        return jpaMovieRepository.getMovieModelByImdbId(imdbId)
                .map(this::mapToMovie)
                .orElse(null);
    }

    private MovieModel mapToMovieModel(MovieDetails movie) {
        Set<MovieActorModel> actors = mapToActorModels(movie.getActors());
        return MovieModel.builder()
                .id(movie.getId())
                .name(movie.getName())
                .plot(movie.getPlot())
                .posterLink(movie.getPoster()
                        .img())
                .actors(actors)
                .imdbId(movie.getImdbId())
                .playerUrl(movie.getPlayer().url())
                .build();
    }

    private MovieDetails mapToMovie(MovieModel movie) {
        Set<MovieActor> actors = mapToMoviesActor(movie.getActors());
        return MovieDetails.createMovieDetails(movie.getId(), movie.getName(), movie.getPlot(), actors,
                new Poster(movie.getPosterLink()), movie.getImdbId());
    }

    private Set<MovieActorModel> mapToActorModels(Collection<MovieActor> actors) {
        return actors
                .stream()
                .map(this::mapToMovieActorModel)
                .collect(Collectors.toSet());
    }

    private MovieActorModel mapToMovieActorModel(MovieActor movieActor) {
        return new MovieActorModel(movieActor.id(), movieActor.name());
    }

    private MovieActor mapToMovieActor(MovieActorModel movieActorModel) {
        return new MovieActor(movieActorModel.id(), movieActorModel.name());
    }

    private Set<MovieActor> mapToMoviesActor(Collection<MovieActorModel> movieActorModels) {
        return movieActorModels
                .stream()
                .map(this::mapToMovieActor)
                .collect(Collectors.toSet());
    }
}
