package com.example.pyramid.model;

import com.example.pyramid.model.enums.OperationType;
import com.example.pyramid.model.enums.TransactionType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalTime;


@Entity
public class Transaction extends _BaseEntity {

    @ManyToOne
    private BankAccount recipientAccount;

    @ManyToOne
    private BankAccount sourceAccount;

    private BigDecimal transactionAmount;

    private LocalTime transactionTime;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

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

    public OperationType getTransactionType() {
        return operationType;
    }

    public void setTransactionType(OperationType operationType) {
        this.operationType = operationType;
    }

    public Transaction(BankAccount sourceAccount , BankAccount recipientAccount, BigDecimal transactionAmount, LocalTime transactionTime, OperationType operationType , TransactionType transactionType) {
        this.sourceAccount = sourceAccount;
        this.recipientAccount = recipientAccount;
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.operationType = operationType;
        this.transactionType = transactionType;
    }

    public Transaction() {
        //nothing here
    }


    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public BankAccount getRecipientAccount() {
        return recipientAccount;
    }

    public void setRecipientAccount(BankAccount recipientAccount) {
        this.recipientAccount = recipientAccount;
    }

    public BankAccount getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(BankAccount sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
}
