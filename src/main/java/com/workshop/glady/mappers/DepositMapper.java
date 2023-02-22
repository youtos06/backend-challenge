package com.workshop.glady.mappers;


import com.workshop.glady.dtos.DepositDto;
import com.workshop.glady.entities.Deposit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface DepositMapper {

    @Mapping(target = "account.id",source = "accountId")
    @Mapping(target = "company.id",source = "companyId")
    @Mapping(source = "type",target = "type")
    Deposit depositDtoToDeposit(DepositDto depositDto);

    @Mapping(source = "id",target = "id")
    @Mapping(source = "account.id",target = "accountId")
    @Mapping(source = "company.id",target = "companyId")
    @Mapping(target = "type",source = "type")
    DepositDto depositToDepositDto(Deposit deposit);
}
