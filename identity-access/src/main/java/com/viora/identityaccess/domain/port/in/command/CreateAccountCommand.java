package com.viora.identityaccess.domain.port.in.command;

import com.viora.identityaccess.domain.model.Role;
import com.viora.identityaccess.domain.vo.AccountId;
import com.viora.identityaccess.domain.vo.Email;

import java.util.Set;

public record CreateAccountCommand(
        AccountId accountId,
        Email email,
        Set<Role> roles
) {
}
