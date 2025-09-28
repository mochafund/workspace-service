package com.mochafund.workspaceservice.account.service;

import com.mochafund.workspaceservice.account.dto.CreateAccountDto;
import com.mochafund.workspaceservice.account.dto.UpdateAccountDto;
import com.mochafund.workspaceservice.account.entity.Account;
import com.mochafund.workspaceservice.account.enums.AccountStatus;
import com.mochafund.workspaceservice.account.repository.IAccountRepository;
import com.mochafund.workspaceservice.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccountService implements IAccountService {

    private final IAccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Account> listAllByWorkspaceId(UUID workspaceId) {
        return accountRepository.findAllByWorkspaceId(workspaceId);
    }

    @Transactional(readOnly = true)
    public Account getAccount(UUID workspaceId, UUID accountId) {
        return accountRepository.findByWorkspaceIdAndId(workspaceId, accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account not found"));
    }

    @Transactional
    public Account createAccount(UUID userId, UUID workspaceId, CreateAccountDto accountDto) {
        Account account = accountRepository.save(Account.builder()
                .workspaceId(workspaceId)
                .createdBy(userId)
                .balance(accountDto.getBalance())
                .name(accountDto.getName())
                .displayName(accountDto.getDisplayName())
                .currency(accountDto.getCurrency())
                .institutionName(accountDto.getInstitutionName())
                .status(accountDto.getStatus())
                .source(accountDto.getSource())
                .type(accountDto.getType())
                .subType(accountDto.getSubType())
                .build());

        log.info("Created accountId={} for name={}", account.getId(), accountDto.getName());
        return account;
    }

    @Transactional
    public Account updateAccount(UUID workspaceId, UUID accountId, UpdateAccountDto accountDto) {
        log.info("Updating accountId={}", accountId);

        Account account = this.getAccount(workspaceId, accountId);
        account.patchFrom(accountDto);

        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(UUID workspaceId, UUID accountId) {
        Account account = this.getAccount(workspaceId, accountId);

        log.info("Deleting accountId={}", account.getId());

        accountRepository.deleteByWorkspaceIdAndId(account.getWorkspaceId(), account.getId());
    }
}
