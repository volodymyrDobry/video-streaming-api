package org.example.viorastreaminggateway.infrastructure.external.content;

import lombok.RequiredArgsConstructor;
import org.example.viorastreaminggateway.infrastructure.external.content.response.MovieResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MovieRestClient {

    private static final String MOVIE_BY_IMDB_IDS_PATH = "/api/v1/movies/movies?imdbIds={imdbIds}";
    private final RestClient contentClient;

    public List<MovieResponse> getMoviesByImdbIds(Set<String> imdbIds) {
        return contentClient.get().uri(MOVIE_BY_IMDB_IDS_PATH, imdbIds)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<List<MovieResponse>>() {
                }).getBody();
    }
}
