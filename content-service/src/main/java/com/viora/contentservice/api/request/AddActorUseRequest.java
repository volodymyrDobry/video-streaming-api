package com.viora.contentservice.api.request;

import jakarta.validation.constraints.NotEmpty;

public record AddActorUseRequest(
        @NotEmpty
        String name
) {
}
