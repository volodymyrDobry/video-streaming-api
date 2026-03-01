package com.viora.identityaccess.api;

import com.viora.identityaccess.api.docs.AccountApi;
import com.viora.identityaccess.api.request.AdminCreateAccountRequest;
import com.viora.identityaccess.api.request.KeycloakCreateAccountRequest;
import com.viora.identityaccess.domain.model.Account;
import com.viora.identityaccess.infrastructure.service.AccountFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountApi {

    private final AccountFacade accountFacade;

    public ResponseEntity<Account> register(@Valid @RequestBody KeycloakCreateAccountRequest request) {
        Account account = accountFacade.createUserAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(account);
    }

    public ResponseEntity<Account> register(@Valid @RequestBody AdminCreateAccountRequest adminCreateAccountRequest) {
        Account account = accountFacade.createAccount(adminCreateAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(account);
    }

}
