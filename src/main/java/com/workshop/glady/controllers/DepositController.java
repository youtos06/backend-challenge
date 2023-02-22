package com.workshop.glady.controllers;

import com.workshop.glady.dtos.AccountDto;
import com.workshop.glady.dtos.DepositDto;
import com.workshop.glady.exceptions.ApiExceptionResponse;
import com.workshop.glady.services.DepositService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/deposit")
@AllArgsConstructor
public class DepositController {

    private DepositService depositService;

    @ApiOperation(value = "Add new deposit")
    @ApiResponses(value = {
            @ApiResponse(code = 201 , response = AccountDto.class , message = "Deposit added with success"),
            @ApiResponse(code = 400 , response = ApiExceptionResponse.class , message = "Exception Message")
    })
    @PostMapping
    public ResponseEntity<DepositDto> storeDeposition(@ApiParam("Deposit detail") @RequestBody @Valid DepositDto depositDto){
        return new ResponseEntity<>(depositService.save(depositDto), HttpStatus.CREATED);
    }
}
