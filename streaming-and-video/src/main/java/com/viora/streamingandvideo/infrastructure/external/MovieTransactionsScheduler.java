package com.viora.streamingandvideo.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viora.app.message.AddVideoPlayerMessage;
import com.viora.streamingandvideo.infrastructure.persistance.MovieTransactionRepository;
import com.viora.streamingandvideo.infrastructure.persistance.model.MovieTransaction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieTransactionsScheduler {

    private final MovieTransactionRepository movieTransactionsRepository;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper jmsObjectMapper;

    @SneakyThrows
    @Scheduled(fixedDelay = 60000)
    public void sendMovieTransactions() {
        log.info("Sending movie transactions to queue");
        List<MovieTransaction> movieTransactions = movieTransactionsRepository.findTop10ByOrderByCreatedAtDesc();
        for (MovieTransaction movieTransaction : movieTransactions) {
            AddVideoPlayerMessage addVideoPlayerMessage = jmsObjectMapper.convertValue(movieTransaction.getPayload(), AddVideoPlayerMessage.class);
            jmsTemplate.convertAndSend("movie-details.queue", addVideoPlayerMessage);
        }
    }


}
