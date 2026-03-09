package com.viora.contentservice.domain.port.in;

import com.viora.contentservice.domain.domain.Actor;

import java.util.Set;

public interface QueryActorsUseCase {

    Set<Actor> getAllActors();

    Set<Actor> getActorsByIds(Set<String> actorIds);

}
