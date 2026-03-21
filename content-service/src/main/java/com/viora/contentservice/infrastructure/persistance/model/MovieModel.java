package com.viora.contentservice.infrastructure.persistance.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document(collection = "movies")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieModel {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Field
    private String name;

    @Field
    private String plot;

    @Field
    private Set<MovieActorModel> actors;

    @Field
    private String posterLink;

    @Field
    private String imdbId;

    @Field
    private String playerUrl;
}
