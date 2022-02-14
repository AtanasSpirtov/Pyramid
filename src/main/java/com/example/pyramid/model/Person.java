package com.example.pyramid.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Person extends _BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Person parent;

    @OneToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;

    @OneToOne
    private Tax taxTypePaid;

    LocalDate taxExpirationDate;

    LocalDate registrationDate;

    public Person(String name, Person parent, BankAccount account , Tax taxTypePaid, LocalDate taxExpirationDate , LocalDate registrationDate) {
        this.name = name;
        this.parent = parent;
        this.account = account;
        this.taxTypePaid = taxTypePaid;
        this.taxExpirationDate = taxExpirationDate;
        this.registrationDate = registrationDate;
    }

    public Person() {}


    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
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

    public Tax getTaxTypePaid() {return taxTypePaid;}

    public void setTaxTypePaid(Tax taxTypePayed) {
        this.taxTypePaid = taxTypePayed;
    }

    public LocalDate getTaxExpirationDate() {
        return taxExpirationDate;
    }

    public void setTaxExpirationDate(LocalDate taxExpirationDate) {
        this.taxExpirationDate = taxExpirationDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }
    public static class PersonBuilder {


        private String name;
        private Person parent;
        private BankAccount bankAccount;
        private Tax taxTypePaid;
        private LocalDate taxExpirationDate;
        private LocalDate registrationDate;

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

        public PersonBuilder setTaxTypePaid(Tax taxTypePaid) {
            this.taxTypePaid = taxTypePaid;
            return this;
        }

        public PersonBuilder setTaxExpirationDate(LocalDate taxExpirationDate) {
            this.taxExpirationDate = taxExpirationDate;
            return this;
        }

        public PersonBuilder setRegistrationDate(LocalDate registrationDate) {
            this.registrationDate = registrationDate;
            return this;
        }

        public Person build() {
            return new Person(name, parent, bankAccount , taxTypePaid, taxExpirationDate , registrationDate);
        }
    }
}
