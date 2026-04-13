package org.example.userstreaminghistory.domain.ports.out;

import org.example.userstreaminghistory.domain.model.History;

import java.util.List;
import java.util.Optional;

public interface UserHistoryRepository {
    List<History> getUserHistories(String userId);

    Optional<History> getUserHistory(String userId, String movieId);

    void saveUserHistory(History history);
}
