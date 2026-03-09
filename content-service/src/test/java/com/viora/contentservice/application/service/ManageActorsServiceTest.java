package com.viora.contentservice.application.service;

import com.viora.contentservice.domain.domain.Actor;
import com.viora.contentservice.domain.exception.ActorNotFoundException;
import com.viora.contentservice.domain.port.in.command.AddActorCommand;
import com.viora.contentservice.domain.port.out.ActorsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageActorsServiceTest {

    @Mock
    private ActorsRepository repository;

    @InjectMocks
    private ManageActorsService service;

    @Test
    void saveActorCreatesDomainActorAndPersistsIt() {
        AddActorCommand command = new AddActorCommand("Keanu Reeves");
        when(repository.saveActor(org.mockito.ArgumentMatchers.any(Actor.class)))
                .thenReturn(new Actor("a5", "Keanu Reeves"));

        Actor result = service.savaActor(command);

        ArgumentCaptor<Actor> captor = ArgumentCaptor.forClass(Actor.class);
        verify(repository).saveActor(captor.capture());
        Actor inputActor = captor.getValue();

        assertThat(inputActor.getId()).isNull();
        assertThat(inputActor.getName()).isEqualTo("Keanu Reeves");
        assertThat(result.getId()).isEqualTo("a5");
        assertThat(result.getName()).isEqualTo("Keanu Reeves");
    }

    @Test
    void getAllActorsReturnsRepositoryResult() {
        Set<Actor> actors = Set.of(new Actor("a1", "Actor One"));
        when(repository.getAllActors()).thenReturn(actors);

        Set<Actor> result = service.getAllActors();

        assertThat(result).isEqualTo(actors);
    }

    @Test
    void getActorsByIdsReturnsActorsWhenAllIdsExist() {
        Set<String> ids = new HashSet<>(Set.of("a1", "a2"));
        Set<Actor> actors = Set.of(new Actor("a1", "Actor One"), new Actor("a2", "Actor Two"));
        when(repository.getActorsByIds(ids)).thenReturn(actors);

        Set<Actor> result = service.getActorsByIds(ids);

        assertThat(result).isEqualTo(actors);
    }

    @Test
    void getActorsByIdsThrowsWhenSomeActorsAreMissing() {
        Set<String> ids = new HashSet<>(Set.of("a1", "a2"));
        when(repository.getActorsByIds(ids)).thenReturn(Set.of(new Actor("a1", "Actor One")));

        assertThatThrownBy(() -> service.getActorsByIds(ids))
                .isInstanceOf(ActorNotFoundException.class)
                .hasMessage("Actors with ids [a2] weren't found");

        assertThat(ids).containsExactly("a2");
    }
}
