package org.example.viorastreaminggateway.infrastructure.external.history;

import lombok.RequiredArgsConstructor;
import org.example.viorastreaminggateway.infrastructure.external.history.response.UserHistoryResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryRestClient {

    private static final String HISTORY_PATH = "/api/v1/user-history";
    private final RestClient userHistoryClient;

    public List<UserHistoryResponse> getHistoriesByUserId(String id) {
        return userHistoryClient.get().uri(HISTORY_PATH).retrieve()
                .toEntity(new ParameterizedTypeReference<List<UserHistoryResponse>>() {
                }).getBody();
    }

}
