package org.example.userstreaminghistory.domain.ports.in;

import org.example.userstreaminghistory.domain.ports.command.SaveUserHistoryCommand;

public interface SaveUserHistoryUseCase {
    void saveUserHistory(SaveUserHistoryCommand command);
}
