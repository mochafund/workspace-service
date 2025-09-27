package com.mochafund.workspaceservice.account.dto;

import com.mochafund.workspaceservice.account.enums.AccountSource;
import com.mochafund.workspaceservice.account.enums.AccountStatus;
import com.mochafund.workspaceservice.account.enums.AccountSubType;
import com.mochafund.workspaceservice.account.enums.AccountType;
import com.mochafund.workspaceservice.account.enums.CurrencyCode;
import com.mochafund.workspaceservice.account.validation.AccountTypeSubTypeCarrier;
import com.mochafund.workspaceservice.account.validation.ValidAccount;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidAccount
public class UpdateAccountDto implements AccountTypeSubTypeCarrier {

    @Size(max = 120, message = "Account name must be at most 120 characters")
    private String name;

    private BigDecimal balance;

    @Size(max = 120, message = "Display name must be at most 120 characters")
    private String displayName;

    @Size(max = 120, message = "Institution name must be at most 120 characters")
    private String institutionName;

    private CurrencyCode currency;

    private AccountSource source;

    private AccountStatus status;

    private AccountType type;

    private AccountSubType subType;
}
