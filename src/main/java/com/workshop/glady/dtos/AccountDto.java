package com.workshop.glady.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto {

    private Long id;

    private BigDecimal giftBalance;

    private BigDecimal mealBalance;

    private Long userId;
}
