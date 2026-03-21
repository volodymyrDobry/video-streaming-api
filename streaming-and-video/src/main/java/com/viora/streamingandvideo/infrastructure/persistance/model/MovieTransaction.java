package com.viora.streamingandvideo.infrastructure.persistance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie_transactions")
public class MovieTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payload_imdb_id")
    private MovieTransactionPayload payload;

    @Column(name = "status", nullable = false)
    private MovieTransactionStatus status;

    public static MovieTransaction createMovieTransaction(MovieTransactionPayload payload) {
        if (payload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        return new MovieTransaction(null, OffsetDateTime.now(), payload, MovieTransactionStatus.CREATED);
    }
}
