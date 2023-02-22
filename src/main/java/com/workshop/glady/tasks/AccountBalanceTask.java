package com.workshop.glady.tasks;

import com.workshop.glady.enums.DepositType;
import com.workshop.glady.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// in real life scenario batch with chunks where we can treat larger scale of data is a better choice

@Component
@AllArgsConstructor
public class AccountBalanceTask {

    private AccountService accountService;

    /**
     * the job is for purpose to update user gift balance , every midnight run
     *  first step:
     *      add new balance to user
     *      ( for example card with start date 12/03/2023 , balance will be added for user at that day)
     *  second step:
     *      check entire consumable balance for user
     *      which means all balance with valid start and end date
     *  third step:
     *      check active balance:
     *      we check if the user balance is greater than the consumable balance
     *          if so we floor the balance down to the consumable balance
     *      if the user balance is equal or less than the consumable balance we let it as it is
     *          means the balance is correct or user might have used a part of the balance ( theory )
     */
    @Scheduled(cron = "${scheduler.gift}")
    public void taskForDailyUpdateOfAccountsGiftBalance(){
        accountService.updateBalanceForAccounts(DepositType.GIFT, LocalDate.now());

    }

    /**
     * same as gift job but only start at the first day of march each year
     * 01/03/**
     */
    @Scheduled(cron ="${scheduler.meal}")
    public void taskForYearlyUpdateOfAccountsMealBalance(){
        accountService.updateBalanceForAccounts(DepositType.MEAL,LocalDate.now());
    }

}
