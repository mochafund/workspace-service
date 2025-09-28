package com.mochafund.workspaceservice.account.repository;

import com.mochafund.workspaceservice.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IAccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByWorkspaceIdAndId(UUID workspaceId, UUID accountId);
    List<Account> findAllByWorkspaceId(UUID workspaceId);
    void deleteByWorkspaceIdAndId(UUID workspaceId, UUID accountId);
}
