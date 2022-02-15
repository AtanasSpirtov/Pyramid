package com.example.pyramid.services;

import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.Transaction;
import com.example.pyramid.model.enums.OperationType;
import com.example.pyramid.model.enums.TransactionType;
import com.example.pyramid.services.api.BankService;
import com.example.pyramid.utils.Calculator;
import com.example.pyramid.utils.Properties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

@Service
public class BankServiceImpl extends _BaseService implements BankService {

    @Override
    @Transactional
    public synchronized void transferMoney(BankAccount source, BankAccount recipient, BigDecimal amount , TransactionType transactionType){
        em.lock(source , LockModeType.PESSIMISTIC_WRITE);
        em.lock(recipient , LockModeType.PESSIMISTIC_WRITE);

        if (Calculator.isNegative(amount)) {
            throw new RuntimeException("parameter amount cannot be negative");
        }

        Objects.requireNonNull(source, "parameter source cannot be null");
        Objects.requireNonNull(recipient, "parameter recipient cannot be null");

        if (!source.getId().equals(Properties.companyClearingAccount)) {
            if (Calculator.isNegative(source.getBalance())) {
                throw new RuntimeException("balance cannot be negative");
            }
            if (Calculator.compare(source.getBalance() , amount) < 0) {
                throw new RuntimeException("amount cannot be bigger than account balance");
            }
        }
        source.setBalance(Calculator.subtractFromFirst(source.getBalance() , amount));
        recipient.setBalance(Calculator.addToFirst(recipient.getBalance() , amount));
        LocalTime localTime = LocalTime.now();
        em.persist(new Transaction(source, recipient, amount, localTime, OperationType.Debit , transactionType));
        em.persist(new Transaction(recipient, source, amount, localTime, OperationType.Credit , transactionType));
    }
}
