package com.mochafund.workspaceservice.account.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountValidator.class)
@Documented
public @interface ValidAccount {

    String message() default "Account type and sub-type combination is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
