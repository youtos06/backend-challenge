package com.workshop.glady.services;

import com.workshop.glady.dtos.DepositDto;
import com.workshop.glady.entities.Account;
import com.workshop.glady.entities.Deposit;
import com.workshop.glady.enums.DepositType;
import com.workshop.glady.exceptions.BusinessApiException;
import com.workshop.glady.mappers.DepositMapper;
import com.workshop.glady.repositories.DepositRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@AllArgsConstructor
@Slf4j
public class DepositService {

    private DepositRepository depositRepository;
    private DepositMapper depositMapper;
    private AccountService accountService;
    private CompanyService companyService;


    public DepositDto save(DepositDto depositDto){
        Deposit deposit = depositMapper.depositDtoToDeposit(depositDto);
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = generateExpirationDate(deposit.getStartDate(),deposit.getType());
        if (expirationDate.isBefore(currentDate)){
            String message = String.format("Start date is too old to be processed : %s",depositDto.getStartDate());
            log.error(message);
            throw new BusinessApiException(message, HttpStatus.BAD_REQUEST);
        }
        deposit.setExpirationDate(expirationDate);
        Account account = accountService.findAccountById(depositDto.getAccountId());
        companyService.extractBalanceFromCompany(depositDto.getCompanyId(),depositDto.getAmount());

        deposit = depositRepository.save(deposit);
        log.info("Deposit saved correctly with id : "+deposit.getId());
        // add to balance if only
        if(this.validSegmentDate(deposit.getStartDate())){
            accountService.setAccountBalance(account,deposit.getType(),deposit.getAmount());
        }
        depositDto = depositMapper.depositToDepositDto(deposit);
        return depositDto;
    }

    private LocalDate generateExpirationDate(LocalDate startDate, DepositType depositType) {
        switch (depositType){
            case GIFT:
                return startDate
                        .plusYears(1)
                        .minusDays(1);
            case MEAL:
                return startDate
                        .plusYears(1)
                        .withMonth(2)
                        .with(TemporalAdjusters.lastDayOfMonth());
            default:
                throw new IllegalArgumentException(String.format(
                        "deposit type not supported , %s",depositType));
        }
    }


    private boolean validSegmentDate(LocalDate startDate){
        LocalDate current = LocalDate.now();

        if (current.isEqual(startDate) || current.isAfter(startDate)){
            return true;
        }
        log.warn("future date segment ,balance will not be added to account directly");
        return false;
    }
}
