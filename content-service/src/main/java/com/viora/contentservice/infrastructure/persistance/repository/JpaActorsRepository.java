package com.viora.contentservice.infrastructure.persistance.repository;

import com.viora.contentservice.infrastructure.persistance.model.ActorModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaActorsRepository extends JpaRepository<ActorModel, Long> {
}
