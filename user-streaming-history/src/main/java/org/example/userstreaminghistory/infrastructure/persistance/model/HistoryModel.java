package org.example.userstreaminghistory.infrastructure.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "histories")
@Data
@AllArgsConstructor
public class HistoryModel {

    @MongoId
    private String id;

    private final String userId;

    private final String movieId;

    private Long segment;
}
