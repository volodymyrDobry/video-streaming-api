package org.example.viorastreaminggateway.domain.ports.out;

import org.example.viorastreaminggateway.domain.model.Movie;

import java.util.List;
import java.util.Set;

public interface MovieRepository {
    List<Movie> getMoviesByImdbIds(Set<String> imdbIds);
}
