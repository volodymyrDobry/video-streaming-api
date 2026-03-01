package com.viora.identityaccess.domain.port.out;

import com.viora.identityaccess.domain.vo.AccountId;

public interface AccountIdGenerator {

    AccountId generateId();

}
