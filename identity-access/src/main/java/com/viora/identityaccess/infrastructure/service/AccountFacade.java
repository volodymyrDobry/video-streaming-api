package com.viora.identityaccess.infrastructure.service;

import com.viora.identityaccess.api.request.AdminCreateAccountRequest;
import com.viora.identityaccess.api.request.KeycloakCreateAccountRequest;
import com.viora.identityaccess.domain.model.Account;

public interface AccountFacade {

    Account createUserAccount(KeycloakCreateAccountRequest request);

    Account createAccount(AdminCreateAccountRequest request);
}
