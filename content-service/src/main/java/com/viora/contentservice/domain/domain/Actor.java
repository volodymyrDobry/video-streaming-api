package com.viora.contentservice.domain.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Actor {
    private Long id;
    private String name;

    public static Actor createActor(String name) {
        return new Actor(null, name);
    }
}
