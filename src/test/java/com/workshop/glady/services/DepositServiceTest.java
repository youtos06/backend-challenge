package com.workshop.glady.services;

import com.workshop.glady.dtos.DepositDto;
import com.workshop.glady.entities.Account;
import com.workshop.glady.entities.Deposit;
import com.workshop.glady.exceptions.BusinessApiException;
import com.workshop.glady.mappers.DepositMapper;
import com.workshop.glady.repositories.DepositRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DepositServiceTest {

    @InjectMocks
    private DepositService depositService;

    @Mock
    private DepositRepository depositRepository;
    @Spy
    private DepositMapper depositMapper = Mappers.getMapper(DepositMapper.class);
    @Mock
    private AccountService accountService;
    @Mock
    private CompanyService companyService;

    @BeforeEach
    void setup(){
        doNothing().when(accountService).setAccountBalance(any(), any(),any());
        Account account = new Account();
        account.setId(1L);
        when(accountService.findAccountById(anyLong())).thenReturn(account);
        when(depositRepository.save(any(Deposit.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    void shouldSaveDepositWithMealExpirationDate() {
        DepositDto depositDto = depositService.save(getDepositDto());
        assertEquals(new BigDecimal(1000),depositDto.getAmount());
        assertEquals(LocalDate.of(2023,2,21),depositDto.getStartDate());
        assertEquals(LocalDate.of(2024,2,29),depositDto.getExpirationDate());
        verify(accountService,times(1)).findAccountById(anyLong());
        verify(depositRepository,times(1)).save(any(Deposit.class));
        verify(companyService,times(1)).extractBalanceFromCompany(anyLong(),any());
    }

    @Test
    void shouldSaveDepositWithGiftExpirationDate() {
        DepositDto depositDto = getDepositDto();
        depositDto.setType("GIFT");
        depositDto = depositService.save(depositDto);
        assertEquals(new BigDecimal(1000),depositDto.getAmount());
        assertEquals(LocalDate.of(2023,2,21),depositDto.getStartDate());
        assertEquals(LocalDate.of(2024,2,20),depositDto.getExpirationDate());
        verify(accountService,times(1)).findAccountById(anyLong());
        verify(depositRepository,times(1)).save(any(Deposit.class));
        verify(companyService,times(1)).extractBalanceFromCompany(anyLong(),any());
        verify(accountService,times(1)).setAccountBalance(any(),any(),any());
    }

    @Test
    void shouldSaveDeposit_ThrowBusinessException() {
        DepositDto depositDto = getDepositDto();
        depositDto.setStartDate(LocalDate.of(2021,1,16));
        Exception exception = assertThrows(BusinessApiException.class,()->depositService.save(depositDto));
        assertEquals("Start date is too old to be processed : 2021-01-16",exception.getMessage());
    }

    @Test
    void shouldSaveDepositWithFutureDateAndNoUpdateOfAccountBalance() {
        DepositDto depositDto = getDepositDto();
        depositDto.setStartDate(LocalDate.of(2025,8,21));
        depositDto.setType("GIFT");
        depositDto = depositService.save(depositDto);
        assertEquals(new BigDecimal(1000),depositDto.getAmount());
        assertEquals(LocalDate.of(2025,8,21),depositDto.getStartDate());
        assertEquals(LocalDate.of(2026,8,20),depositDto.getExpirationDate());
        verify(accountService,times(1)).findAccountById(anyLong());
        verify(accountService,never()).setAccountBalance(any(),any(),any());
        verify(depositRepository,times(1)).save(any(Deposit.class));
    }

    private DepositDto getDepositDto(){
        DepositDto depositDto = new DepositDto();
        depositDto.setId(1L);
        depositDto.setStartDate(LocalDate.of(2023,2,21));
        depositDto.setAmount(new BigDecimal(1000));
        depositDto.setAccountId(1L);
        depositDto.setCompanyId(1L);
        depositDto.setType("MEAL");
        return depositDto;
    }
}