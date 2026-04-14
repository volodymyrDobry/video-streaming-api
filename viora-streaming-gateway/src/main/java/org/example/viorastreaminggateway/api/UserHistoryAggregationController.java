package org.example.viorastreaminggateway.api;

import lombok.RequiredArgsConstructor;
import org.example.viorastreaminggateway.application.service.UserHistoryAggregationService;
import org.example.viorastreaminggateway.domain.model.AggregatedUserHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-history")
@RequiredArgsConstructor
public class UserHistoryAggregationController {

    private final UserHistoryAggregationService userHistoryAggregationService;

    @GetMapping("/aggregated")
    public ResponseEntity<List<AggregatedUserHistory>> getAggregatedHistory() {
        return ResponseEntity.ok(userHistoryAggregationService.getAggregatedUserHistory());
    }
}
