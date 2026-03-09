package com.viora.contentservice.domain.domain;

import com.viora.contentservice.domain.vo.MovieActor;
import com.viora.contentservice.domain.vo.Player;
import com.viora.contentservice.domain.vo.Poster;
import com.viora.contentservice.domain.vo.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

import static com.viora.contentservice.domain.helper.DomainCreatorChecker.checkDomainValues;

// Stopped on adding fields
@Getter
public class MovieDetails {

    private String id;

    // Title rename to title
    private String name;

    private String plot;

    private Set<MovieActor> actors;

    private Poster poster;

    private String imdbId;

    @Setter
    private Player player;

    private MovieDetails(String id, String name, String plot, Set<MovieActor> actors, Poster poster, String imdbId) {
        this.id = id;
        this.name = name;
        this.plot = plot;
        this.actors = actors;
        this.poster = poster;
        this.imdbId = imdbId;
    }

    public static MovieDetails createMovieDetails(String name, String plot, Set<MovieActor> actors, Poster poster, String imdbId) {
        return createMovieDetails(null, name, plot, actors, poster, imdbId);
    }

    public static MovieDetails createMovieDetails(String id, String name, String plot, Set<MovieActor> actors, Poster poster, String imdbId) {
        checkDomainValues(name, plot, actors, poster, imdbId);
        return new MovieDetails(id, name, plot, actors, poster, imdbId);
    }
}
