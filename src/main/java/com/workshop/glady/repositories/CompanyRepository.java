package com.workshop.glady.repositories;


import com.workshop.glady.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    @Override
    Optional<Company> findById(Long aLong);
}
