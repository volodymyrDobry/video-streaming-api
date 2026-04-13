package org.example.userstreaminghistory.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class History {
    private final String id;
    private final String userId;
    private final String movieId;
    private Long segment;
}
