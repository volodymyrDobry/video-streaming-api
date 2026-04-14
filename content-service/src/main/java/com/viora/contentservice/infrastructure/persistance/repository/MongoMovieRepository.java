package com.viora.contentservice.infrastructure.persistance.repository;

import com.viora.contentservice.domain.vo.MovieSummary;
import com.viora.contentservice.infrastructure.persistance.model.MovieModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface MongoMovieRepository extends MongoRepository<MovieModel, String> {

    Set<MovieModel> findAllByNameContaining(String name);

    Optional<MovieModel> getMovieModelById(String id);

    Optional<MovieModel> getMovieModelByImdbId(String imdbId);

    Set<MovieModel> getAllByImdbIdIn(Collection<String> imdbIds);
}
