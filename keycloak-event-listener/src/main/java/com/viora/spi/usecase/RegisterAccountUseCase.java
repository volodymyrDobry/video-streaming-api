package com.viora.spi.usecase;

import com.viora.spi.usecase.command.RegisterUserCommand;

public interface RegisterAccountUseCase {

    void registerAccount(RegisterUserCommand registerUserCommand);

}
