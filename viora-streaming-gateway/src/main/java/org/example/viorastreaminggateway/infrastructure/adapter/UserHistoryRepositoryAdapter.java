package org.example.viorastreaminggateway.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.example.viorastreaminggateway.domain.model.UserHistory;
import org.example.viorastreaminggateway.domain.ports.out.UserHistoryRepository;
import org.example.viorastreaminggateway.infrastructure.external.history.HistoryRestClient;
import org.example.viorastreaminggateway.infrastructure.external.history.response.UserHistoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHistoryRepositoryAdapter implements UserHistoryRepository {

    private final HistoryRestClient historyRestClient;

    @Override
    public List<UserHistory> getUserHistories(String userId) {
        return historyRestClient.getHistoriesByUserId(userId)
                .stream()
                .map(this::mapToUserHistory)
                .toList();
    }


    private UserHistory mapToUserHistory(UserHistoryResponse response) {
        return new UserHistory(response.userId(), response.movieId(), response.segment());
    }
}
