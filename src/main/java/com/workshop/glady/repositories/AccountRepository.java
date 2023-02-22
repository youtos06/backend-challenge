package com.workshop.glady.repositories;

import com.workshop.glady.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Override
    Optional<Account> findById(Long aLong);
}
