package com.mochafund.workspaceservice.account.dto;

import com.mochafund.workspaceservice.account.enums.AccountSource;
import com.mochafund.workspaceservice.account.enums.AccountStatus;
import com.mochafund.workspaceservice.account.enums.AccountSubType;
import com.mochafund.workspaceservice.account.enums.AccountType;
import com.mochafund.workspaceservice.account.enums.CurrencyCode;
import com.mochafund.workspaceservice.account.validation.AccountTypeSubTypeCarrier;
import com.mochafund.workspaceservice.account.validation.ValidAccount;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateAccountDto implements AccountTypeSubTypeCarrier {

    @NotBlank(message = "Account name must be provided")
    @Size(max = 120, message = "Account name must be at most 120 characters")
    private String name;

    private BigDecimal balance = BigDecimal.ZERO;

    @Size(max = 120, message = "Display name must be at most 120 characters")
    private String displayName;

    @Size(max = 120, message = "Institution name must be at most 120 characters")
    private String institutionName;

    private CurrencyCode currency = CurrencyCode.USD;

    private AccountSource source = AccountSource.MANUAL;

    @NotNull(message = "Account type must be provided")
    private AccountType type;

    @NotNull(message = "Account sub-type must be provided")
    private AccountSubType subType;
}
