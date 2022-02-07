package com.example.pyramid.model;

import javax.persistence.*;

@Entity
public class Person extends _BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Person parent;

    @OneToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;

    public void setAccount(BankAccount account) {
        this.account = account;
    }

    public BankAccount getAccount() {
        return account;
    }

    public Person getParent() {
        return parent;
    }

    public void setParent(Person parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
