package org.example.viorastreaminggateway.domain.ports.in;

import org.example.viorastreaminggateway.domain.model.AggregatedUserHistory;

import java.util.List;

public interface GetAggregatedUserHistoryUseCase {
    List<AggregatedUserHistory> getAggregatedUserHistory();
}
