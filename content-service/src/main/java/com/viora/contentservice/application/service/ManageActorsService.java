package com.viora.contentservice.application.service;

import com.viora.contentservice.domain.domain.Actor;
import com.viora.contentservice.domain.port.in.ManageActorsUseCase;
import com.viora.contentservice.domain.port.in.QueryActorsUseCase;
import com.viora.contentservice.domain.port.in.command.AddActorCommand;
import com.viora.contentservice.domain.port.out.ActorsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ManageActorsService implements ManageActorsUseCase, QueryActorsUseCase {

    private final ActorsRepository repository;

    @Override
    public Actor savaActor(AddActorCommand command) {
        Actor actor = Actor.createActor(command.name());
        actor = repository.saveActor(actor);
        return actor;
    }

    @Override
    public Set<Actor> getAllActors() {
        return repository.getAllActors();
    }
}
