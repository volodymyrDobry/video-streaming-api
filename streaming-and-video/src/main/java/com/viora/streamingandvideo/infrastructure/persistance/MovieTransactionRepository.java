package com.viora.streamingandvideo.infrastructure.persistance;

import com.viora.streamingandvideo.infrastructure.persistance.model.MovieTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieTransactionRepository extends JpaRepository<MovieTransaction, Long> {

    List<MovieTransaction> findTop10ByOrderByCreatedAtDesc();

}
