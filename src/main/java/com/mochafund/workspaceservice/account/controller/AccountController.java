package com.mochafund.workspaceservice.account.controller;

import com.mochafund.workspaceservice.account.dto.AccountDto;
import com.mochafund.workspaceservice.account.dto.CreateAccountDto;
import com.mochafund.workspaceservice.account.dto.UpdateAccountDto;
import com.mochafund.workspaceservice.account.entity.Account;
import com.mochafund.workspaceservice.account.service.IAccountService;
import com.mochafund.workspaceservice.common.annotations.UserId;
import com.mochafund.workspaceservice.common.annotations.WorkspaceId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final IAccountService accountService;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountDto>> getAllAccounts(@WorkspaceId UUID workspaceId) {
        List<Account> accounts = accountService.listAllByWorkspaceId(workspaceId);
        return ResponseEntity.ok().body(AccountDto.fromEntities(accounts));
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> getAccount(@WorkspaceId UUID workspaceId, @PathVariable UUID accountId) {
        Account account = accountService.getAccount(workspaceId, accountId);
        return ResponseEntity.ok().body(AccountDto.fromEntity(account));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> createAccount(
            @UserId UUID userId, @WorkspaceId UUID workspaceId,
            @Valid @RequestBody CreateAccountDto accountDto
    ) {
        Account account = accountService.createAccount(userId, workspaceId, accountDto);
        return ResponseEntity.status(201).body(AccountDto.fromEntity(account));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PatchMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> updateAccount(
            @WorkspaceId UUID workspaceId, @PathVariable UUID accountId,
            @Valid @RequestBody UpdateAccountDto accountDto) {
        Account account = accountService.updateAccount(workspaceId, accountId, accountDto);
        return ResponseEntity.ok().body(AccountDto.fromEntity(account));
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @DeleteMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAccount(@WorkspaceId UUID workspaceId, @PathVariable UUID accountId) {
        accountService.deleteAccount(workspaceId, accountId);
        return ResponseEntity.noContent().build();
    }
}
