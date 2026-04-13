package org.example.userstreaminghistory.infrastructure.persistance;

import org.example.userstreaminghistory.domain.model.History;
import org.example.userstreaminghistory.infrastructure.persistance.model.HistoryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoUserHistoryRepository extends MongoRepository<HistoryModel, String> {
    List<HistoryModel> findAllByUserId(String userId);

    Optional<HistoryModel> findByUserIdAndMovieId(String userId, String movieId);
}
