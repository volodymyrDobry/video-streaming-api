package com.viora.contentservice.infrastructure.persistance.adapter;

import com.viora.contentservice.domain.domain.Actor;
import com.viora.contentservice.infrastructure.persistance.repository.MongoActorsRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
@Import(JpaActorsRepositoryAdapter.class)
class JpaActorsRepositoryAdapterIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.12");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private JpaActorsRepositoryAdapter adapter;

    @Autowired
    private MongoActorsRepository mongoActorsRepository;

    @AfterEach
    void tearDown() {
        mongoActorsRepository.deleteAll();
    }

    @Test
    void saveActorPersistsAndRetrievesByIds() {
        Actor saved = adapter.saveActor(Actor.createActor("Keanu Reeves"));

        assertThat(saved.getId()).isNotBlank();
        assertThat(ObjectId.isValid(saved.getId())).isTrue();

        Set<Actor> actors = adapter.getActorsByIds(Set.of(saved.getId()));
        assertThat(actors).hasSize(1);
        assertThat(actors.iterator().next().getName()).isEqualTo("Keanu Reeves");
    }
}
