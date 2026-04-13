package org.example.userstreaminghistory.domain.ports.in;

import org.example.userstreaminghistory.domain.model.History;

import java.util.List;

public interface GetUserHistoriesUseCase {
    List<History> getUserHistories(String userId);

    History getUserHistory(String userId, String movieId);
}
