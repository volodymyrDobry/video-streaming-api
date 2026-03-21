package com.viora.contentservice.infrastructure.external.message;

import java.time.OffsetDateTime;

public record StreamingAndVideoEvent(
        Long id,
        OffsetDateTime createdAt,
        StreamingAndVideoPayload payload,
        MovieTransactionStatus status
) {
}
