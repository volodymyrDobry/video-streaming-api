package com.viora.contentservice.infrastructure.persistance.repository;

import com.viora.contentservice.infrastructure.persistance.model.ActorModel;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
class MongoActorsRepositoryIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.12");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoActorsRepository mongoActorsRepository;

    @AfterEach
    void tearDown() {
        mongoActorsRepository.deleteAll();
    }

    @Test
    void saveWithoutIdGeneratesMongoObjectIdAndCanFindById() {
        ActorModel actor = new ActorModel(null, "Keanu Reeves");

        ActorModel saved = mongoActorsRepository.save(actor);

        assertThat(saved.getId()).isNotBlank();
        assertThat(ObjectId.isValid(saved.getId())).isTrue();
        assertThat(mongoActorsRepository.findAllById(Set.of(saved.getId()))).hasSize(1);
    }
}
