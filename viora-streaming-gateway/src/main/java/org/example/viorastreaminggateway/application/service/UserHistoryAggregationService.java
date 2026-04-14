package org.example.viorastreaminggateway.application.service;

import lombok.RequiredArgsConstructor;
import org.example.viorastreaminggateway.domain.model.AggregatedUserHistory;
import org.example.viorastreaminggateway.domain.model.Movie;
import org.example.viorastreaminggateway.domain.model.UserHistory;
import org.example.viorastreaminggateway.domain.ports.in.GetAggregatedUserHistoryUseCase;
import org.example.viorastreaminggateway.domain.ports.out.MovieRepository;
import org.example.viorastreaminggateway.domain.ports.out.UserHistoryRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserHistoryAggregationService implements GetAggregatedUserHistoryUseCase {

    private final UserHistoryRepository userHistoryRepository;
    private final MovieRepository movieRepository;


    @Override
    public List<AggregatedUserHistory> getAggregatedUserHistory() {
        List<UserHistory> userHistories = userHistoryRepository.getUserHistories(getUserId());
        Set<String> movieIds = userHistories.stream().map(UserHistory::movieId).collect(Collectors.toSet());

        List<Movie> movies = movieRepository.getMoviesByImdbIds(movieIds);
        Map<String, Movie> movieIdMap = mapToMovieIdMap(movies);

        return userHistories.stream().map(h -> {
            Movie movieById = movieIdMap.get(h.movieId());
            return new AggregatedUserHistory(h.movieId(), h.segment(), movieById.title(), movieById.poster());
        }).toList();
    }

    private Map<String, Movie> mapToMovieIdMap(List<Movie> movies) {
        return movies.stream().collect(Collectors.toMap(Movie::movieId, movie -> movie));
    }

    private String getUserId() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getSubject();
        }
        throw new RuntimeException("No user logged in");
    }

}
