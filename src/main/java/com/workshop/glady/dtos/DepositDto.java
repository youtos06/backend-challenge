package com.workshop.glady.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.workshop.glady.constraints.annotations.DepositTypeConstraint;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepositDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "Deposit amount is mandatory")
    private BigDecimal amount;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private LocalDate expirationDate;

    @DepositTypeConstraint
    private String type;

    @NotNull(message = "company id is mandatory to make a deposit")
    private Long companyId;

    @NotNull(message = "account id is mandatory to make a deposit")
    private Long accountId;
}
