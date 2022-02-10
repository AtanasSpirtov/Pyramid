package com.example.pyramid.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class BankAccount extends _BaseEntity {

    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}