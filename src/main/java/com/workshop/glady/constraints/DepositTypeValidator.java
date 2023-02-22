package com.workshop.glady.constraints;

import com.workshop.glady.constraints.annotations.DepositTypeConstraint;
import com.workshop.glady.enums.DepositType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

public class DepositTypeValidator implements ConstraintValidator<DepositTypeConstraint,String> {
    @Override
    public void initialize(DepositTypeConstraint depositTypeConstraint) {
        // Init
    }

    @Override
    public boolean isValid(String type, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.isNull(type) || Arrays.stream(DepositType.values()).anyMatch(g -> g.name().equals(type));
    }
}
