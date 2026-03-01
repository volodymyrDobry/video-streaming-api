package com.viora.identityaccess.domain.port.in;

import com.viora.identityaccess.domain.model.Account;
import com.viora.identityaccess.domain.port.in.command.CreateAccountCommand;

public interface ManageUserAccountUseCase {

    Account createAccount(CreateAccountCommand createAccountCommand);

}
