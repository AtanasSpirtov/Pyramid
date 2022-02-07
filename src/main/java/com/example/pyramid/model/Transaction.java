package com.example.pyramid.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalTime;


@Entity
public class Transaction extends _BaseEntity {

    @ManyToOne
    private Person recipientAccount;

    @ManyToOne
    private Person sourceAccount;

    private BigDecimal transactionAmount;

    private LocalTime transactionTime;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public LocalTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Transaction(Person sourceAccount , Person recipientAccount, BigDecimal transactionAmount, LocalTime transactionTime, TransactionType transactionType) {
        this.sourceAccount = sourceAccount;
        this.recipientAccount = recipientAccount;
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.transactionType = transactionType;
    }

    public Transaction() {
        //nothing here
    }


    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public Person getRecipientAccount() {
        return recipientAccount;
    }

    public void setRecipientAccount(Person recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public Person getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Person sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
}
