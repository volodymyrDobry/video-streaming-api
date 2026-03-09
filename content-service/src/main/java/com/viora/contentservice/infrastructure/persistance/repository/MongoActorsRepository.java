package com.viora.contentservice.infrastructure.persistance.repository;

import com.viora.contentservice.infrastructure.persistance.model.ActorModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoActorsRepository extends MongoRepository<ActorModel, String> {
}
