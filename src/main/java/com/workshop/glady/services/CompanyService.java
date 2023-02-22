package com.workshop.glady.services;

import com.workshop.glady.entities.Company;
import com.workshop.glady.exceptions.BusinessApiException;
import com.workshop.glady.repositories.CompanyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyService {
    private CompanyRepository companyRepository;

    public void extractBalanceFromCompany(Long companyId, BigDecimal balance){
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> {
                    log.error("Company id isn't associated to an existing company");
                    throw new BusinessApiException("Company id isn't associated to an existing company", HttpStatus.BAD_REQUEST);
                }
        );
        if (company.getBalance().compareTo(balance)< 0){
            log.error(String.format("Company balance don't allow transaction : %h",company.getBalance()));
            throw new BusinessApiException("Company balance don't allow transaction", HttpStatus.BAD_REQUEST);
        }
        company.setBalance(company.getBalance().subtract(balance));
    }
}
