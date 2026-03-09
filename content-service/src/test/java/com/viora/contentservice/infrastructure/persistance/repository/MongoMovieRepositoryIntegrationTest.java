package com.viora.contentservice.infrastructure.persistance.repository;

import com.viora.contentservice.infrastructure.persistance.model.MovieActorModel;
import com.viora.contentservice.infrastructure.persistance.model.MovieModel;
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
class MongoMovieRepositoryIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.12");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MongoMovieRepository mongoMovieRepository;

    @AfterEach
    void tearDown() {
        mongoMovieRepository.deleteAll();
    }

    @Test
    void saveWithoutIdGeneratesMongoObjectIdAndCanReadByIdAndImdbId() {
        MovieModel movie = MovieModel.builder()
                .name("Inception")
                .plot("Dreams")
                .actors(Set.of(new MovieActorModel("a1", "Actor One")))
                .posterLink("poster.png")
                .imdbId("tt1375666")
                .build();

        MovieModel saved = mongoMovieRepository.save(movie);

        assertThat(saved.getId()).isNotBlank();
        assertThat(ObjectId.isValid(saved.getId())).isTrue();

        assertThat(mongoMovieRepository.getMovieModelById(saved.getId())).isPresent();
        assertThat(mongoMovieRepository.getMovieModelByImdbId("tt1375666")).isPresent();
    }
}
