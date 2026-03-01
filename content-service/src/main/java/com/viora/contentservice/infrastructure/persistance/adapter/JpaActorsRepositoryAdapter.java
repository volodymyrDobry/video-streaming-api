package com.viora.contentservice.infrastructure.persistance.adapter;

import com.viora.contentservice.domain.domain.Actor;
import com.viora.contentservice.domain.port.out.ActorsRepository;
import com.viora.contentservice.infrastructure.persistance.model.ActorModel;
import com.viora.contentservice.infrastructure.persistance.repository.JpaActorsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaActorsRepositoryAdapter implements ActorsRepository {

    private final JpaActorsRepository jpaActorsRepository;

    @Override
    public Actor saveActor(Actor actor) {
        ActorModel actorModel = mapToActorModel(actor);
        actorModel = jpaActorsRepository.save(actorModel);
        return mapToActor(actorModel);
    }

    @Override
    public Set<Actor> getActorsByIds(Set<Long> ids) {
        return jpaActorsRepository.findAllById(ids)
                .stream()
                .map(JpaActorsRepositoryAdapter::mapToActor)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Actor> getAllActors() {
        return jpaActorsRepository.findAll()
                .stream()
                .map(JpaActorsRepositoryAdapter::mapToActor)
                .collect(Collectors.toSet());
    }

    protected static Actor mapToActor(ActorModel actorModel) {
        return new Actor(actorModel.getId(), actorModel.getName());
    }

    protected static Set<Actor> mapToActors(Collection<ActorModel> actors) {
        return actors
                .stream()
                .map(JpaActorsRepositoryAdapter::mapToActor)
                .collect(Collectors.toSet());
    }

    protected static Set<ActorModel> mapToActorModels(Collection<Actor> actors) {
        return actors
                .stream()
                .map(JpaActorsRepositoryAdapter::mapToActorModel)
                .collect(Collectors.toSet());
    }

    protected static ActorModel mapToActorModel(Actor actor) {
        return new ActorModel(actor.getId(), actor.getName());
    }
}
