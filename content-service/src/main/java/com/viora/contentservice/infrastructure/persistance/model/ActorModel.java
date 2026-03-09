package com.viora.contentservice.infrastructure.persistance.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "actors")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorModel {
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    @Field
    private String name;

}
