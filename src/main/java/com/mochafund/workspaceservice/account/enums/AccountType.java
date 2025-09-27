package com.mochafund.workspaceservice.account.enums;

import com.mochafund.workspaceservice.common.exception.BadRequestException;

import java.util.EnumSet;

public enum AccountType {
    CASH,
    CREDIT,
    INVESTMENT,
    LOAN,
    REAL_ESTATE,
    VEHICLE,
    EMPLOYEE_COMPENSATION,
    OTHER_LIABILITY,
    OTHER_ASSET;

    public EnumSet<AccountSubType> allowedSubTypes() {
        EnumSet<AccountSubType> result = EnumSet.noneOf(AccountSubType.class);
        for (AccountSubType subType : AccountSubType.values()) {
            if (subType.supports(this)) {
                result.add(subType);
            }
        }
        return result;
    }

    public void validateSubType(AccountSubType subType) {
        if (subType == null) return;
        if (!subType.supports(this)) {
            throw new BadRequestException("Account type " + this + " does not support sub-type " + subType);
        }
    }
}
