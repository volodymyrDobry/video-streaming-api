package com.viora.streamingandvideo.infrastructure.persistance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieTransactionPayload {
    @Id
    private String imdbId;

    @Column(name = "playback_url", nullable = false, unique = true)
    private String playbackUrl;
}
