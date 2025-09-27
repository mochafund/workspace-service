package com.mochafund.workspaceservice.account.dto;

import com.mochafund.workspaceservice.account.entity.Account;
import com.mochafund.workspaceservice.account.enums.AccountSource;
import com.mochafund.workspaceservice.account.enums.AccountStatus;
import com.mochafund.workspaceservice.account.enums.AccountSubType;
import com.mochafund.workspaceservice.account.enums.AccountType;
import com.mochafund.workspaceservice.account.enums.CurrencyCode;
import com.mochafund.workspaceservice.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class AccountDto extends BaseDto {

    private UUID workspaceId;
    private UUID createdBy;
    private BigDecimal balance;
    private String name;
    private CurrencyCode currency;
    private String displayName;
    private String institutionName;
    private AccountSource source;
    private AccountStatus status;
    private AccountType type;
    private AccountSubType subType;

    public static AccountDto fromEntity(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .workspaceId(account.getWorkspaceId())
                .createdBy(account.getCreatedBy())
                .balance(account.getBalance())
                .name(account.getName())
                .currency(account.getCurrency())
                .displayName(account.getDisplayName())
                .institutionName(account.getInstitutionName())
                .source(account.getSource())
                .status(account.getStatus())
                .type(account.getType())
                .subType(account.getSubType())
                .build();
    }

    public static List<AccountDto> fromEntities(List<Account> accounts) {
        return accounts.stream().map(AccountDto::fromEntity).toList();
    }
}
