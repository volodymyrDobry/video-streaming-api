package com.viora.identityaccess.domain.model;

import com.viora.identityaccess.domain.vo.AccountId;
import com.viora.identityaccess.domain.vo.CreatedAt;
import com.viora.identityaccess.domain.vo.Email;

import java.util.Collections;
import java.util.Set;

public record Account(AccountId id, Email email,CreatedAt createdAt, Set<Role> roles) {

    @Override
    public Set<Role> roles() {
        return Collections.unmodifiableSet(this.roles);
    }

}
