package com.viora.contentservice.domain.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

import static com.viora.contentservice.domain.domain.DomainCreatorChecker.checkDomainValues;

@Getter
@AllArgsConstructor
public class MovieDetails {

    private Long id;

    private String name;

    private String plot;

    private Set<Actor> actors;

    private Poster poster;

    public static MovieDetails createMovieDetails(String name, String plot, Set<Actor> actors, Poster poster) {
        checkDomainValues(name, plot, actors, poster);
        return new MovieDetails(null, name, plot, actors, poster);
    }
}
