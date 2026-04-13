package org.example.userstreaminghistory.domain.ports.command;

public record SaveUserHistoryCommand(
        String userId, String movieId, Long segment
) {
}
