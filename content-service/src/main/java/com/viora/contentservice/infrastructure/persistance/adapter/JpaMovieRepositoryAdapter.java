package com.viora.contentservice.infrastructure.persistance.adapter;

import com.viora.contentservice.domain.domain.Actor;
import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.domain.Poster;
import com.viora.contentservice.domain.port.out.MovieDetailsRepository;
import com.viora.contentservice.domain.vo.MovieSummary;
import com.viora.contentservice.infrastructure.persistance.model.ActorModel;
import com.viora.contentservice.infrastructure.persistance.model.MovieModel;
import com.viora.contentservice.infrastructure.persistance.repository.JpaMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.viora.contentservice.infrastructure.persistance.adapter.JpaActorsRepositoryAdapter.mapToActors;

@Repository
@RequiredArgsConstructor
public class JpaMovieRepositoryAdapter implements MovieDetailsRepository {

    private final JpaMovieRepository jpaMovieRepository;

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
                .map(m -> new MovieSummary(m.getName(), new Poster(m.getPosterLink())))
                .collect(Collectors.toSet());
    }

    @Override
    public MovieDetails getMovieDetailsById(Long id) {
        Optional<MovieModel> movie = jpaMovieRepository.getMovieByIdWithActors(id);
        return movie.map(this::mapToMovie)
                .orElse(null);
    }

    @Override
    public Set<MovieSummary> getAllMovies() {
        return jpaMovieRepository.findAll()
                .stream()
                .map(m -> new MovieSummary(m.getName(), new Poster(m.getPosterLink())))
                .collect(Collectors.toSet());
    }

    private MovieModel mapToMovieModel(MovieDetails movie) {
        Set<ActorModel> actors = mapToActorModels(movie.getActors());
        return MovieModel.builder()
                .id(movie.getId())
                .name(movie.getName())
                .plot(movie.getPlot())
                .posterLink(movie.getPoster()
                        .img())
                .actors(actors)
                .build();
    }

    private MovieDetails mapToMovie(MovieModel movie) {
        Set<Actor> actors = mapToActors(movie.getActors());
        return new MovieDetails(movie.getId(), movie.getName(), movie.getName(), actors,
                new Poster(movie.getPosterLink()));
    }

    private Set<ActorModel> mapToActorModels(Collection<Actor> actors) {
        return actors
                .stream()
                .map(JpaActorsRepositoryAdapter::mapToActorModel)
                .collect(Collectors.toSet());
    }
}
