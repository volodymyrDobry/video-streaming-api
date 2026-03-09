package com.viora.contentservice.infrastructure.persistance.adapter;

import com.viora.contentservice.domain.domain.MovieDetails;
import com.viora.contentservice.domain.vo.MovieActor;
import com.viora.contentservice.domain.vo.Poster;
import com.viora.contentservice.infrastructure.persistance.repository.MongoMovieRepository;
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
@Import(JpaMovieRepositoryAdapter.class)
class JpaMovieRepositoryAdapterIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.12");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private JpaMovieRepositoryAdapter adapter;

    @Autowired
    private MongoMovieRepository mongoMovieRepository;

    @AfterEach
    void tearDown() {
        mongoMovieRepository.deleteAll();
    }

    @Test
    void saveMovieDetailsPersistsAndRetrievesByIdAndImdbId() {
        MovieDetails movie = MovieDetails.createMovieDetails(
                "Inception",
                "Dreams",
                Set.of(new MovieActor("a1", "Actor One")),
                new Poster("poster.png"),
                "tt1375666"
        );

        MovieDetails saved = adapter.saveMovieDetails(movie);

        assertThat(saved.getId()).isNotBlank();
        assertThat(ObjectId.isValid(saved.getId())).isTrue();

        MovieDetails byId = adapter.getMovieDetailsById(saved.getId());
        assertThat(byId).isNotNull();
        assertThat(byId.getName()).isEqualTo("Inception");

        MovieDetails byImdbId = adapter.getMovieByImdbId("tt1375666");
        assertThat(byImdbId).isNotNull();
        assertThat(byImdbId.getId()).isEqualTo(saved.getId());
    }
}
