package com.viora.contentservice.domain.port.out;

import com.viora.contentservice.domain.domain.Actor;

import java.util.Set;

public interface ActorsRepository {

    Actor saveActor(Actor actor);

    Set<Actor> getActorsByIds(Set<Long> ids);

    Set<Actor> getAllActors();

}
