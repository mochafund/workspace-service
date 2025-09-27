package com.mochafund.workspaceservice.account.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum AccountSubType {

    CHECKING(AccountType.CASH),
    SAVINGS(AccountType.CASH),
    DIGITAL_WALLET(AccountType.CASH),
    GIFT_CARD(AccountType.CASH),
    PHYSICAL_CASH(AccountType.CASH),
    OTHER_CASH(AccountType.CASH),

    CREDIT_CARD(AccountType.CREDIT),
    CHARGE_CARD(AccountType.CREDIT),
    LINE_OF_CREDIT(AccountType.CREDIT),
    OTHER_CREDIT(AccountType.CREDIT),

    RETIREMENT(AccountType.INVESTMENT),
    EDUCATION_SAVINGS(AccountType.INVESTMENT),
    BROKERAGE(AccountType.INVESTMENT),
    HEALTH_SAVINGS(AccountType.INVESTMENT),
    OTHER_INVESTMENT(AccountType.INVESTMENT),

    MORTGAGE(AccountType.LOAN),
    AUTO_LOAN(AccountType.LOAN),
    STUDENT_LOAN(AccountType.LOAN),
    PERSONAL_LOAN(AccountType.LOAN),
    OTHER_LOAN(AccountType.LOAN),

    PRIMARY_RESIDENCE(AccountType.REAL_ESTATE),
    INVESTMENT_PROPERTY(AccountType.REAL_ESTATE),
    LAND(AccountType.REAL_ESTATE),
    OTHER_REAL_ESTATE(AccountType.REAL_ESTATE),

    CAR(AccountType.VEHICLE),
    TRUCK(AccountType.VEHICLE),
    MOTORCYCLE(AccountType.VEHICLE),
    BOAT(AccountType.VEHICLE),
    RV(AccountType.VEHICLE),
    OTHER_VEHICLE(AccountType.VEHICLE),

    STOCK_OPTIONS(AccountType.EMPLOYEE_COMPENSATION),
    RESTRICTED_STOCK(AccountType.EMPLOYEE_COMPENSATION),
    ESPP(AccountType.EMPLOYEE_COMPENSATION),
    BONUS(AccountType.EMPLOYEE_COMPENSATION),
    OTHER_COMPENSATION(AccountType.EMPLOYEE_COMPENSATION),

    TAXES_PAYABLE(AccountType.OTHER_LIABILITY),
    MEDICAL_BILL(AccountType.OTHER_LIABILITY),
    LEGAL_OBLIGATION(AccountType.OTHER_LIABILITY),
    OTHER_LIABILITY(AccountType.OTHER_LIABILITY),

    JEWELRY(AccountType.OTHER_ASSET),
    COLLECTIBLE(AccountType.OTHER_ASSET),
    LIFE_INSURANCE_CASH_VALUE(AccountType.OTHER_ASSET),
    OTHER_ASSET(AccountType.OTHER_ASSET);

    private final EnumSet<AccountType> supportedTypes;

    AccountSubType(AccountType... supportedTypes) {
        if (supportedTypes == null || supportedTypes.length == 0) {
            this.supportedTypes = EnumSet.noneOf(AccountType.class);
        } else {
            this.supportedTypes = EnumSet.copyOf(Arrays.asList(supportedTypes));
        }
    }

    public boolean supports(AccountType type) {
        return type != null && supportedTypes.contains(type);
    }

    public Set<AccountType> supportedTypes() {
        return Collections.unmodifiableSet(supportedTypes);
    }
}
