package com.workshop.glady.entities;



import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.workshop.glady.enums.DepositType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "DEPOSIT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private DepositType type;

    @ManyToOne
    private Company company;

    @ManyToOne
    @JsonManagedReference
    @JsonIgnoreProperties("user")
    private Account account;
}
