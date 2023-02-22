package com.workshop.glady.repositories;

import com.workshop.glady.entities.Deposit;
import com.workshop.glady.enums.DepositType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface DepositRepository extends JpaRepository<Deposit,Long> {

    /*@Query("select d from DEPOSIT d where d.account.id = ?1 and d.type = ?2 and d.expirationDate > ?3 and d.startDate <= ?3")
    List<Deposit> findByAccountAndTypeAndValidDate(long account_id, DepositType depositType, LocalDate date);

    List<Deposit> findByAccountIdAndTypeAndExpirationDateGreaterThanAndStartDateLessThanEqual(long account_id, DepositType depositType, LocalDate date1,LocalDate date2);*/

    @Query("select COALESCE(SUM(d.amount),0) from DEPOSIT d where d.account.id = ?1 and d.type = ?2 and d.expirationDate > ?3 and d.startDate <= ?3")
    BigDecimal calculateConsumableByAccountAndTypeAndValidDate(long accountId, DepositType depositType, LocalDate date);

    @Query("select COALESCE(SUM(d.amount),0) from DEPOSIT d where d.account.id = ?1 and d.type = ?2 and  d.startDate = ?3")
    BigDecimal calculateDayConsumableAccountIdAndTypeAndStartDate(long accountId, DepositType depositType, LocalDate date);
}
