package com.viora.contentservice.infrastructure.external;

import com.viora.contentservice.infrastructure.external.command.SaveMovieVideoCommand;
import com.viora.streamingandvideo.domain.ports.in.SaveMovieUseCase;
import com.viora.streamingandvideo.domain.ports.in.command.SaveMovieCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreamingServiceImpl implements StreamingService {

    private final SaveMovieUseCase saveMovieUseCase;

    @Override
    public void saveMovie(SaveMovieVideoCommand command) {
        SaveMovieCommand saveMovieCommand = new SaveMovieCommand(command.id()
                .toString(), command.name(), command.movie());
        saveMovieUseCase.saveMovie(saveMovieCommand);
    }
}
