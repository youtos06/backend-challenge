package com.workshop.glady.controllers;

import com.workshop.glady.dtos.AccountDto;
import com.workshop.glady.exceptions.ApiExceptionHandler;
import com.workshop.glady.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    MockMvc mockMvc;

    @Mock
    private AccountService accountService;
    @BeforeEach
    void setUp(){

        mockMvc = MockMvcBuilders.standaloneSetup(accountController).setControllerAdvice(new ApiExceptionHandler()).build();
    }

    @Test
    void shouldReturnAccountByGetDepositConsumable() throws Exception {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setMealBalance(new BigDecimal(100));
        accountDto.setGiftBalance(new BigDecimal("150.50"));
        accountDto.setUserId(1L);

        when(accountService.getAccountDtoById(anyLong())).thenReturn(accountDto);
        verify(accountService,never()).getAccountByVerification(anyLong());
        mockMvc.perform(get("/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.giftBalance").value(new BigDecimal("150.5")));
    }

    @Test
    void shouldReturnAccountByGetDepositVerifiedConsumable() throws Exception {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setMealBalance(new BigDecimal(100));
        accountDto.setGiftBalance(new BigDecimal("150.50"));
        accountDto.setUserId(1L);

        when(accountService.getAccountByVerification(anyLong())).thenReturn(accountDto);
        verify(accountService,never()).getAccountDtoById(anyLong());
        mockMvc.perform(get("/account/1?verify=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.giftBalance").value(new BigDecimal("150.5")));
    }

}