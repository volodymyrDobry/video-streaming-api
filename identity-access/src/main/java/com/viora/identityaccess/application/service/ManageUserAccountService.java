package com.viora.identityaccess.application.service;

import com.viora.identityaccess.domain.exception.AlreadyExistsException;
import com.viora.identityaccess.domain.model.Account;
import com.viora.identityaccess.domain.port.in.ManageUserAccountUseCase;
import com.viora.identityaccess.domain.port.in.command.CreateAccountCommand;
import com.viora.identityaccess.domain.port.out.AccountRepository;
import com.viora.identityaccess.domain.vo.CreatedAt;
import com.viora.identityaccess.domain.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ManageUserAccountService implements ManageUserAccountUseCase {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(CreateAccountCommand command) {
        Email email = command.email();
        if (accountRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Account with this email already exists");
        }
        CreatedAt createdAt = new CreatedAt(LocalDateTime.now());
        Account account = new Account(command.accountId(), email, createdAt, command.roles());
        account = accountRepository.saveAccount(account);

        return account;
    }
}
