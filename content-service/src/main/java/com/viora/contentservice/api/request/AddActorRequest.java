package com.viora.contentservice.api.request;

import jakarta.validation.constraints.NotEmpty;

public record AddActorRequest(
        @NotEmpty
        String name
) {
}
