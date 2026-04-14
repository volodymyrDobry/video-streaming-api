package org.example.userstreaminghistory.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.userstreaminghistory.domain.model.History;
import org.example.userstreaminghistory.domain.ports.in.GetUserHistoriesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-history")
@RequiredArgsConstructor
public class UserHistoryController {

    private final GetUserHistoriesUseCase getUserHistoriesUseCase;

    // TODO need to get user id
    @GetMapping
    public ResponseEntity<List<History>> getUserHistories() {
        return ResponseEntity.ok(getUserHistoriesUseCase.getUserHistories("1"));
    }

    // TODO need to get user id
    @GetMapping("/{movieId}")
    public ResponseEntity<History> getUserHistory(@PathVariable String movieId) {
        return ResponseEntity.ok(getUserHistoriesUseCase.getUserHistory("1", movieId));
    }


}
