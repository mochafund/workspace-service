package com.mochafund.workspaceservice.account.validation;

import com.mochafund.workspaceservice.account.enums.AccountSubType;
import com.mochafund.workspaceservice.account.enums.AccountType;

/**
 * Marker interface so validators can read the type/sub-type pairing
 * from DTOs without knowing their concrete class.
 */
public interface AccountTypeSubTypeCarrier {

    AccountType getType();

    AccountSubType getSubType();
}
