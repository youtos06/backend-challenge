package com.workshop.glady.mappers;


import com.workshop.glady.dtos.AccountDto;
import com.workshop.glady.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "user.id",target = "userId")
    AccountDto accountToAccountDto(Account account);

    @Mapping(target = "user.id",source = "userId")
    Account accountDtoToAccount(AccountDto accountDto);
}
