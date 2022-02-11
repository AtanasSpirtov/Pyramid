package com.example.pyramid.services.api;

import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.enums.TransactionType;

import java.math.BigDecimal;

public interface BankService{
    void transferMoney(BankAccount source, BankAccount recipient , BigDecimal amount , TransactionType transactionType);
}
