package com.viora.identityaccess.infrastructure.persistance.adapter;

import com.viora.identityaccess.domain.model.Account;
import com.viora.identityaccess.domain.model.Role;
import com.viora.identityaccess.domain.port.out.AccountRepository;
import com.viora.identityaccess.domain.vo.Email;
import com.viora.identityaccess.infrastructure.persistance.model.AccountEntity;
import com.viora.identityaccess.infrastructure.persistance.model.RoleEntity;
import com.viora.identityaccess.infrastructure.persistance.repository.JpaAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final JpaAccountRepository jpaAccountRepository;


    @Override
    public Account saveAccount(Account account) {
        AccountEntity accountEntity = mapToAccountEntity(account);
        jpaAccountRepository.save(accountEntity);
        return account;
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaAccountRepository.existsByEmail(email.email());
    }

    private AccountEntity mapToAccountEntity(Account account) {
        return AccountEntity.builder()
                .id(account.id()
                        .id())
                .email(account.email()
                        .email())
                .createdAt(account.createdAt()
                        .cratedAt())
                .roles(mapToRoleEntities(account.roles()))
                .build();
    }

    private Set<RoleEntity> mapToRoleEntities(Collection<Role> roles) {
        return roles.stream()
                .map(this::mapToRoleEntity)
                .collect(Collectors.toSet());
    }

    private RoleEntity mapToRoleEntity(Role role) {
        return new RoleEntity(role.id(), role.name());
    }

}
