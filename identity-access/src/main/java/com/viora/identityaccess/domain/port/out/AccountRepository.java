package com.viora.identityaccess.domain.port.out;

import com.viora.identityaccess.domain.model.Account;
import com.viora.identityaccess.domain.vo.Email;

public interface AccountRepository {

    Account saveAccount(Account account);


    boolean existsByEmail(Email email);
}
