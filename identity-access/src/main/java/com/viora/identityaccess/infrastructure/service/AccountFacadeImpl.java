package com.viora.identityaccess.infrastructure.service;

import com.viora.identityaccess.api.request.AdminCreateAccountRequest;
import com.viora.identityaccess.api.request.KeycloakCreateAccountRequest;
import com.viora.identityaccess.domain.model.Account;
import com.viora.identityaccess.domain.model.Role;
import com.viora.identityaccess.domain.port.in.ManageUserAccountUseCase;
import com.viora.identityaccess.domain.port.in.command.CreateAccountCommand;
import com.viora.identityaccess.domain.vo.AccountId;
import com.viora.identityaccess.domain.vo.Email;
import com.viora.identityaccess.infrastructure.keycloak.KeycloakClient;
import com.viora.identityaccess.infrastructure.keycloak.model.KeycloakRole;
import com.viora.identityaccess.infrastructure.keycloak.request.CreateKeycloakAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountFacadeImpl implements AccountFacade {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ManageUserAccountUseCase manageUserAccountUseCase;
    private final KeycloakClient keycloakClient;


    @Override
    public Account createUserAccount(KeycloakCreateAccountRequest request) {
        Set<ERole> roles = Set.of(ERole.USER);
        CreateAccountCommand createAccountCommand = mapToCreateUserAccount(request, roles);
        Set<KeycloakRole> keycloakRoles = mapToKeycloakRoles(roles);
        scheduler.schedule(() -> {
            keycloakClient.assignRoles(request.id(), keycloakRoles);
        }, 2, TimeUnit.SECONDS);
        return createAccount(createAccountCommand);
    }

    @Override
    public Account createAccount(AdminCreateAccountRequest request) {
        Set<KeycloakRole> keycloakRoles = mapToKeycloakRoles(request.roles());
        CreateKeycloakAccountRequest keycloakAccountRequest =
                new CreateKeycloakAccountRequest(request.email(), keycloakRoles, request.password());
        String accountId = keycloakClient.createAccount(keycloakAccountRequest);
        CreateAccountCommand createAccountCommand =
                mapToCreateUserAccount(new KeycloakCreateAccountRequest(accountId,
                        request.email()), request.roles());

        return createAccount(createAccountCommand);
    }

    private Account createAccount(CreateAccountCommand command) {
        try {
            return manageUserAccountUseCase.createAccount(command);
        } catch (Exception e) {
            keycloakClient.deleteAccount(command.accountId()
                    .id());
            throw new RuntimeException(e);
        }
    }

    private CreateAccountCommand mapToCreateUserAccount(KeycloakCreateAccountRequest request, Set<ERole> roles) {
        AccountId accountId = new AccountId(request.id());
        Email email = new Email(request.email());
        Set<Role> commandRoles = roles.stream()
                .map(ERole::getPersistentValue)
                .collect(Collectors.toSet());
        return new CreateAccountCommand(accountId, email, commandRoles);
    }

    private Set<KeycloakRole> mapToKeycloakRoles(Set<ERole> roles) {
        return roles.stream()
                .map(ERole::getKeycloakValue)
                .collect(Collectors.toSet());
    }
}
