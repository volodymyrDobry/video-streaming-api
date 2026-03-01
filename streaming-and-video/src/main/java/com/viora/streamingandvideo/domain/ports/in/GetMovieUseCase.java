package com.viora.streamingandvideo.domain.ports.in;

import org.springframework.core.io.Resource;

public interface GetMovieUseCase {
    Resource getMoviePlayback(Long id);

    Resource getMovieSegment(Long id, Long segmentId);
}
