package com.example.pyramid.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class BankAccount extends _BaseEntity {

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Person owner;

    private BigDecimal balance;

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Person getOwner() {
        return owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}