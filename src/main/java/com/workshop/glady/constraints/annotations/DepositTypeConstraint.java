package com.workshop.glady.constraints.annotations;

import com.workshop.glady.constraints.DepositTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Constraint(validatedBy = DepositTypeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DepositTypeConstraint {
    String message() default "Deposit type is not supported";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
