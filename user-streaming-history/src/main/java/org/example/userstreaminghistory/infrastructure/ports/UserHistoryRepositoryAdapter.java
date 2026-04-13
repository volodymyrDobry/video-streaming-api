package org.example.userstreaminghistory.infrastructure.ports;

import lombok.RequiredArgsConstructor;
import org.example.userstreaminghistory.domain.model.History;
import org.example.userstreaminghistory.domain.ports.out.UserHistoryRepository;
import org.example.userstreaminghistory.infrastructure.persistance.MongoUserHistoryRepository;
import org.example.userstreaminghistory.infrastructure.persistance.model.HistoryModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserHistoryRepositoryAdapter implements UserHistoryRepository {

    private final MongoUserHistoryRepository mongoUserHistoryRepository;

    @Override
    public List<History> getUserHistories(String userId) {
        return mongoUserHistoryRepository.findAllByUserId(userId).stream()
                .map(this::mapToHistory)
                .toList();
    }

    @Override
    public Optional<History> getUserHistory(String userId, String movieId) {
        return mongoUserHistoryRepository.findByUserIdAndMovieId(userId, movieId)
                .map(this::mapToHistory);
    }

    @Override
    public void saveUserHistory(History history) {
        HistoryModel newHistoryModel = getUserHistory(history.getUserId(), history.getMovieId())
                .map(model -> new HistoryModel(model.getId(), model.getUserId(), model.getMovieId(), history.getSegment()))
                .orElse(this.mapToHistoryModel(history));
        mongoUserHistoryRepository.save(newHistoryModel);
    }

    private HistoryModel mapToHistoryModel(History history) {
        return new HistoryModel(history.getId(), history.getUserId(), history.getMovieId(), history.getSegment());
    }

    private History mapToHistory(HistoryModel history) {
        return new History(history.getId(), history.getUserId(), history.getMovieId(), history.getSegment());
    }
}
