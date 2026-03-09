package com.viora.identityaccess.application.service;

import com.viora.identityaccess.domain.exception.AlreadyExistsException;
import com.viora.identityaccess.domain.model.Account;
import com.viora.identityaccess.domain.model.Role;
import com.viora.identityaccess.domain.port.in.command.CreateAccountCommand;
import com.viora.identityaccess.domain.port.out.AccountRepository;
import com.viora.identityaccess.domain.vo.AccountId;
import com.viora.identityaccess.domain.vo.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageUserAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ManageUserAccountService service;

    @Test
    void createAccountThrowsWhenEmailAlreadyExists() {
        CreateAccountCommand command = new CreateAccountCommand(
                new AccountId("acc-1"),
                new Email("user@example.com"),
                Set.of(new Role(1L, "USER"))
        );
        when(accountRepository.existsByEmail(command.email())).thenReturn(true);

        assertThatThrownBy(() -> service.createAccount(command))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessage("Account with this email already exists");

        verify(accountRepository, never()).saveAccount(any());
    }

    @Test
    void createAccountBuildsAndSavesAccountWhenEmailIsUnique() {
        CreateAccountCommand command = new CreateAccountCommand(
                new AccountId("acc-1"),
                new Email("user@example.com"),
                Set.of(new Role(1L, "USER"))
        );
        when(accountRepository.existsByEmail(command.email())).thenReturn(false);
        when(accountRepository.saveAccount(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime beforeCall = LocalDateTime.now();
        Account result = service.createAccount(command);
        LocalDateTime afterCall = LocalDateTime.now();

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).saveAccount(accountCaptor.capture());
        Account saved = accountCaptor.getValue();

        assertThat(result).isSameAs(saved);
        assertThat(saved.id()).isEqualTo(command.accountId());
        assertThat(saved.email()).isEqualTo(command.email());
        assertThat(saved.roles()).isEqualTo(command.roles());
        assertThat(saved.createdAt().cratedAt()).isBetween(beforeCall.minusSeconds(1), afterCall.plusSeconds(1));
    }
}
