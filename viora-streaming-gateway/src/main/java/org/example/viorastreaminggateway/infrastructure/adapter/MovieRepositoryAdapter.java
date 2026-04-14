package org.example.viorastreaminggateway.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.example.viorastreaminggateway.domain.model.Movie;
import org.example.viorastreaminggateway.domain.ports.out.MovieRepository;
import org.example.viorastreaminggateway.infrastructure.external.content.MovieRestClient;
import org.example.viorastreaminggateway.infrastructure.external.content.response.MovieResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MovieRepositoryAdapter implements MovieRepository {

    private final MovieRestClient movieRestClient;

    @Override
    public List<Movie> getMoviesByImdbIds(Set<String> imdbIds) {
        return movieRestClient.getMoviesByImdbIds(imdbIds)
                .stream()
                .map(this::mapToMovie)
                .toList();
    }


    private Movie mapToMovie(MovieResponse response) {
        return new Movie(response.id(), response.name(), response.poster().url());
    }

}
