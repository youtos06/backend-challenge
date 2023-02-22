package com.workshop.glady.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity(name = "ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "gift_balance")
    private BigDecimal giftBalance;

    @Column(name = "meal_balance")
    private BigDecimal mealBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnoreProperties("accounts")
    private User user;

    @OneToMany(mappedBy = "account")
    @JsonBackReference
    private List<Deposit> deposits;
}
