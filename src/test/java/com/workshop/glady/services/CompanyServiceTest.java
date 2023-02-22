package com.workshop.glady.services;

import com.workshop.glady.entities.Company;
import com.workshop.glady.exceptions.BusinessApiException;
import com.workshop.glady.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @BeforeEach
    public void setup(){

    }

    @Test
    void shouldExtractBalanceFromCompany() {
        when(companyRepository.findById(anyLong())).thenReturn(getCompany());
        companyService.extractBalanceFromCompany(1L,new BigDecimal(100));
        verify(companyRepository,times(1)).findById(anyLong());
    }

    @Test
    void shouldNotBiggerBalanceFromCompany_ThrowBusinessException() {
        when(companyRepository.findById(anyLong())).thenReturn(getCompany());
        Exception exception = assertThrows(BusinessApiException.class,
                ()->companyService.extractBalanceFromCompany(1L,new BigDecimal(10000)));
        verify(companyRepository,times(1)).findById(anyLong());
        assertEquals("Company balance don't allow transaction",exception.getMessage());
    }

    @Test
    void shouldNotFindCompanyById_ThrowBusinessException() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(BusinessApiException.class,
                ()->companyService.extractBalanceFromCompany(1L,new BigDecimal(100)));
        verify(companyRepository,times(1)).findById(anyLong());
        assertEquals("Company id isn't associated to an existing company",exception.getMessage());
    }



    private Optional<Company> getCompany(){
        return Optional.of(new Company(1L,"company",new BigDecimal(1000)));
    }
}