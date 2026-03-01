package com.viora.spi.usecase.command;

public record RegisterUserCommand(
        String id,
        String email
) {
}
