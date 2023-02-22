package com.workshop.glady.services;


import com.workshop.glady.dtos.AccountDto;
import com.workshop.glady.entities.Account;
import com.workshop.glady.enums.DepositType;
import com.workshop.glady.exceptions.BusinessApiException;
import com.workshop.glady.mappers.AccountMapper;
import com.workshop.glady.repositories.AccountRepository;
import com.workshop.glady.repositories.DepositRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.workshop.glady.enums.DepositType.MEAL;

@Service
@AllArgsConstructor
@Slf4j
public class AccountService {

    private AccountRepository accountRepository;
    private DepositRepository depositRepository;
    private AccountMapper accountMapper;

    public AccountDto getAccountByVerification(long accountId){
        Account account = findAccountById(accountId);
        // ACCOUNT WILL BE UPDATED IN JOB FOR CORRECT SYNC
        AccountDto accountDto = accountMapper.accountToAccountDto(account);

        BigDecimal consumableGiftBalance = depositRepository.calculateConsumableByAccountAndTypeAndValidDate(
                accountId,
                DepositType.GIFT,
                LocalDate.now());
        if (accountDto.getGiftBalance().compareTo(consumableGiftBalance)>0){
            log.info(String.format("It seems account %d have more than the consumable gift balance",accountId));
            accountDto.setGiftBalance(consumableGiftBalance);
        }

        BigDecimal consumableMealBalance = depositRepository.calculateConsumableByAccountAndTypeAndValidDate(
                accountId,
                DepositType.MEAL,
                LocalDate.now());
        if (accountDto.getMealBalance().compareTo(consumableMealBalance)>0){
            log.info(String.format("It seems account %d have more than the consumable meal balance",accountId));
            accountDto.setMealBalance(consumableMealBalance);
        }
        return accountDto;
    }

    public AccountDto getAccountDtoById(long accountId){
        return accountMapper.accountToAccountDto(findAccountById(accountId));
    }

    public Account findAccountById(long accountId){
        return accountRepository.findById(accountId).orElseThrow(
                () -> new BusinessApiException("Account id isn't associated to an existing account", HttpStatus.NOT_FOUND)
        );
    }

    public void setAccountBalance(Account account, DepositType depositType , BigDecimal amount){
        if(depositType.equals(DepositType.GIFT)){
            account.setGiftBalance(account.getGiftBalance().add(amount));
        }else{
            account.setMealBalance(account.getMealBalance().add(amount));
        }
        accountRepository.save(account);
    }

    public void updateBalanceForAccounts(DepositType depositType,LocalDate localDate){
        List<Account> accounts = accountRepository.findAll();
        // add new day consumable balance
        for (Account account : accounts){
            updateAccountBalance(account,depositType,localDate);
        }
    }

    public void updateAccountBalance(Account account,DepositType depositType,LocalDate localDate){
        updateBalanceByTypeOfAccountAndDay(account,depositType,localDate);
        // get total consumable balance
        BigDecimal consumableBalance = depositRepository.calculateConsumableByAccountAndTypeAndValidDate(
                account.getId(),depositType,localDate
        );
        // get total consumable balance on user active balance
        if(depositType == MEAL){
            if(account.getMealBalance().compareTo(consumableBalance)>0) account.setMealBalance(consumableBalance);
        }else {
            if(account.getGiftBalance().compareTo(consumableBalance)>0) account.setGiftBalance(consumableBalance);
        }
        accountRepository.save(account);
    }

    private void updateBalanceByTypeOfAccountAndDay(Account account, DepositType depositType, LocalDate localDate){

        BigDecimal dayConsumableBalance = depositRepository.calculateDayConsumableAccountIdAndTypeAndStartDate(
                account.getId(),depositType,localDate
        );
        if(depositType == MEAL){
            account.setMealBalance(account.getMealBalance().add(dayConsumableBalance));
        }else {
            account.setGiftBalance(account.getMealBalance().add(dayConsumableBalance));
        }
    }
}
