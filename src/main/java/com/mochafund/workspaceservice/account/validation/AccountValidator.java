package com.mochafund.workspaceservice.account.validation;

import com.mochafund.workspaceservice.account.enums.AccountSubType;
import com.mochafund.workspaceservice.account.enums.AccountType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountValidator implements ConstraintValidator<ValidAccount, AccountTypeSubTypeCarrier> {

    @Override
    public boolean isValid(AccountTypeSubTypeCarrier carrier, ConstraintValidatorContext context) {
        if (carrier == null) {
            return true;
        }

        AccountType type = carrier.getType();
        AccountSubType subType = carrier.getSubType();

        if (type == null || subType == null) {
            return true;
        }

        if (subType.supports(type)) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                        String.format("subType %s is not allowed for type %s", subType, type))
                .addPropertyNode("subType")
                .addConstraintViolation();

        return false;
    }
}
