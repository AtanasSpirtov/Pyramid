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

    public Person(String name, Person parent, BankAccount account) {
        this.name = name;
        this.parent = parent;
        this.account = account;
    }

    public Person() {}


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

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    public static class PersonBuilder {

        private String name;
        private Person parent;
        private BankAccount bankAccount;

        public PersonBuilder setName(final String name) {
            this.name = name;
            return this;
        }

        public PersonBuilder setParent(final Person parent) {
            this.parent = parent;
            return this;
        }

        public PersonBuilder setBankAccount(final BankAccount bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public Person build() {
            return new Person(name, parent, bankAccount);
        }
    }
}
