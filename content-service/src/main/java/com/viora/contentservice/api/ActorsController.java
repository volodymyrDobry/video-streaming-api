package com.viora.contentservice.api;

import com.viora.contentservice.api.docs.ActorsApi;
import com.viora.contentservice.api.request.AddActorUseRequest;
import com.viora.contentservice.domain.domain.Actor;
import com.viora.contentservice.domain.port.in.ManageActorsUseCase;
import com.viora.contentservice.domain.port.in.QueryActorsUseCase;
import com.viora.contentservice.domain.port.in.command.AddActorCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class ActorsController implements ActorsApi {

    private final ManageActorsUseCase manageActorsUseCase;
    private final QueryActorsUseCase queryActorsUseCase;

    public ResponseEntity<Actor> addActor(AddActorUseRequest request) {
        AddActorCommand addActorCommand = new AddActorCommand(request.name());
        Actor actor = manageActorsUseCase.savaActor(addActorCommand);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(actor);
    }

    public ResponseEntity<Set<Actor>> getAllActors() {
        Set<Actor> actors = queryActorsUseCase.getAllActors();
        return ResponseEntity.ok(actors);
    }


}
