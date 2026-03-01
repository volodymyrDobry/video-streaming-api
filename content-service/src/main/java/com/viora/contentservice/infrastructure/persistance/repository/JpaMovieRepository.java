package com.viora.contentservice.infrastructure.persistance.repository;

import com.viora.contentservice.domain.vo.MovieSummary;
import com.viora.contentservice.infrastructure.persistance.model.MovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface JpaMovieRepository extends JpaRepository<MovieModel, Long> {

    Set<MovieModel> findAllByNameContaining(String name);

    @Query("SELECT m FROM MovieModel m JOIN FETCH m.actors WHERE m.id = :id")
    Optional<MovieModel> getMovieByIdWithActors(@Param("id") Long id);
}
