package com.mochafund.workspaceservice.account.service;

import com.mochafund.workspaceservice.account.dto.CreateAccountDto;
import com.mochafund.workspaceservice.account.dto.UpdateAccountDto;
import com.mochafund.workspaceservice.account.entity.Account;

import java.util.List;
import java.util.UUID;

public interface IAccountService {
    Account createAccount(UUID userId, UUID workspaceId, CreateAccountDto accountDto);
    Account getAccount(UUID workspaceId, UUID accountId);
    Account updateAccount(UUID workspaceId, UUID accountId, UpdateAccountDto accountDto);
    void deleteAccount(UUID workspaceId, UUID accountId);
    List<Account> listAllByWorkspaceId(UUID workspaceId);
}