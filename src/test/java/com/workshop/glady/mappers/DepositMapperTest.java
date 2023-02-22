package com.workshop.glady.mappers;

import com.workshop.glady.dtos.DepositDto;
import com.workshop.glady.entities.Account;
import com.workshop.glady.entities.Company;
import com.workshop.glady.entities.Deposit;
import com.workshop.glady.enums.DepositType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepositMapperTest {

    @Spy
    DepositMapper depositMapper = Mappers.getMapper(DepositMapper.class);

    @Test
    void shouldConvertDepositDtoToDeposit() {
        DepositDto depositDto = new DepositDto();
        depositDto.setStartDate(LocalDate.now());
        depositDto.setAmount(new BigDecimal(1000));
        depositDto.setAccountId(1L);
        depositDto.setCompanyId(1L);
        depositDto.setType("MEAL");

        Deposit deposit = depositMapper.depositDtoToDeposit(depositDto);

        assertNotNull(deposit);
        assertEquals(DepositType.MEAL,deposit.getType());
        assertEquals(new BigDecimal(1000),deposit.getAmount());
        assertEquals(1L,deposit.getAccount().getId());
    }

    @Test
    void shouldConvertDepositToDepositDto() {
        Deposit deposit = new Deposit();
        deposit.setStartDate(LocalDate.now());
        deposit.setAmount(new BigDecimal(1000));
        Account account = new Account();
        account.setId(1L);
        deposit.setAccount(account);
        Company company = new Company();
        company.setId(1L);
        deposit.setCompany(company);
        deposit.setType(DepositType.MEAL);

        DepositDto depositDto = depositMapper.depositToDepositDto(deposit);

        assertNotNull(depositDto);
        assertEquals("MEAL",depositDto.getType());
        assertEquals(new BigDecimal(1000),depositDto.getAmount());
        assertEquals(1L,depositDto.getAccountId());
        assertEquals(1L,depositDto.getCompanyId());
    }
}