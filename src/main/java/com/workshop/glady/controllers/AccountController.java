package com.workshop.glady.controllers;

import com.workshop.glady.dtos.AccountDto;
import com.workshop.glady.exceptions.ApiExceptionResponse;
import com.workshop.glady.services.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;


    /**
     *  normally should be an auth on this endpoint
     * @param accountId
     * @param verify account active balance with consumable balance
     * @return user account
     */
    @ApiOperation(value = "Retrieve account by id",
            notes = "verify active means the balance will be checked if it surpasses active balance by date")
    @ApiResponses(value = {
            @ApiResponse(code = 200 , response = AccountDto.class , message = "Account retrieved by success"),
            @ApiResponse(code = 404 , response = ApiExceptionResponse.class , message = "Exception Message")
    })
    @ApiParam(name = "verify",required = false, value = "Consumable balance verification, return limit of consumable")
    @GetMapping("/{accountId}")
    public AccountDto getDepositsConsumable(
            @PathVariable(name = "accountId") Long accountId,
            @RequestParam(name = "verify",required = false,defaultValue = "false") boolean verify){
        if (verify){
            return accountService.getAccountByVerification(accountId);
        }
        return accountService.getAccountDtoById(accountId);
    }
}
