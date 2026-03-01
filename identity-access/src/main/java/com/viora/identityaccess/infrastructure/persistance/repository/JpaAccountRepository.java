package com.viora.identityaccess.infrastructure.persistance.repository;

import com.viora.identityaccess.infrastructure.persistance.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsByEmail(String email);
}
