package org.example.viorastreaminggateway.domain.ports.out;

import org.example.viorastreaminggateway.domain.model.UserHistory;

import java.util.List;

public interface UserHistoryRepository {

    List<UserHistory> getUserHistories(String userId);

}
