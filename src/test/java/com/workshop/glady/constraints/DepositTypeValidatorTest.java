package com.workshop.glady.constraints;

import com.workshop.glady.dtos.DepositDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;

class DepositTypeValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValidDepositDot(){
        DepositDto depositDto = new DepositDto();
        depositDto.setStartDate(LocalDate.now());
        depositDto.setAmount(new BigDecimal(1000));
        depositDto.setAccountId(1L);
        depositDto.setCompanyId(1L);
        depositDto.setType("MEAL");
        Set<ConstraintViolation<DepositDto>> violations = validator.validate(depositDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeUnValidDepositDot_ByType(){
        DepositDto depositDto = new DepositDto();
        depositDto.setStartDate(LocalDate.now());
        depositDto.setAmount(new BigDecimal(1000));
        depositDto.setAccountId(1L);
        depositDto.setCompanyId(1L);
        depositDto.setType("Bla");
        Set<ConstraintViolation<DepositDto>> violations = validator.validate(depositDto);
        assertFalse(violations.isEmpty());
        assertEquals("Deposit type is not supported",violations.iterator().next().getMessage());
    }

    @Test
    void shouldBeUnValidDepositDot_MissingRequiredCompanyId(){
        DepositDto depositDto = new DepositDto();
        depositDto.setStartDate(LocalDate.now());
        depositDto.setAmount(new BigDecimal(1000));
        depositDto.setAccountId(1L);
        depositDto.setType("GIFT");
        Set<ConstraintViolation<DepositDto>> violations = validator.validate(depositDto);
        assertFalse(violations.isEmpty());
        assertEquals("company id is mandatory to make a deposit",violations.iterator().next().getMessage());
    }
}