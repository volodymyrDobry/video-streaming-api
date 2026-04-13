package org.example.userstreaminghistory.application.service;

import lombok.RequiredArgsConstructor;
import org.example.userstreaminghistory.domain.exception.HistoryNotFoundException;
import org.example.userstreaminghistory.domain.model.History;
import org.example.userstreaminghistory.domain.ports.in.GetUserHistoriesUseCase;
import org.example.userstreaminghistory.domain.ports.in.SaveUserHistoryUseCase;
import org.example.userstreaminghistory.domain.ports.command.SaveUserHistoryCommand;
import org.example.userstreaminghistory.domain.ports.out.UserHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHistoryService implements GetUserHistoriesUseCase, SaveUserHistoryUseCase {

    private final UserHistoryRepository userHistoryRepository;

    @Override
    public void saveUserHistory(SaveUserHistoryCommand command) {
        History newHistory = new History(null, command.userId(), command.movieId(), command.segment());
        userHistoryRepository.saveUserHistory(newHistory);
    }

    @Override
    public List<History> getUserHistories(String userId) {
        return userHistoryRepository.getUserHistories(userId);
    }

    @Override
    public History getUserHistory(String userId, String movieId) {
        return userHistoryRepository.getUserHistory(userId, movieId)
                .orElseThrow(() -> new HistoryNotFoundException("History for user %s and movie %s not found".formatted(userId, movieId)));
    }
}
