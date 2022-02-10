package com.example.pyramid.services;

import com.example.pyramid.model.Person;
import com.example.pyramid.model.Transaction;
import com.example.pyramid.model.OperationType;
import com.example.pyramid.model.TransactionType;
import com.example.pyramid.services.api.BankService;
import com.example.pyramid.utils.Calculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

@Service
public class BankServiceImpl extends _BaseService implements BankService {

    private static final Long companyClearingAccount = -1L;

    @Override
    @Transactional
    public synchronized void transferMoney(Person source, Person recipient, BigDecimal amount , TransactionType transactionType){
        em.lock(source , LockModeType.PESSIMISTIC_WRITE);
        em.lock(recipient , LockModeType.PESSIMISTIC_WRITE);

        if (Calculator.isNegative(amount)) {
            throw new RuntimeException("amount cannot be negative");
        }

        Objects.requireNonNull(source, "not existing user");
        Objects.requireNonNull(recipient, "not existing user");

        if (!source.getId().equals(companyClearingAccount)) {
            if (Calculator.isNegative(source.getAccount().getBalance())) {
                throw new RuntimeException("balance cannot be negative");
            }
            if (Calculator.compare(source.getAccount().getBalance() , amount) < 0) {
                throw new RuntimeException("amount cannot be bigger than account balance");
            }
        }
        source.getAccount().setBalance(Calculator.subtractFromFirst(source.getAccount().getBalance() , amount));
        recipient.getAccount().setBalance(Calculator.addToFirst(recipient.getAccount().getBalance() , amount));
        LocalTime localTime = LocalTime.now();
        em.persist(new Transaction(source.getAccount(), recipient.getAccount(), amount, localTime, OperationType.Debit , transactionType));
        em.persist(new Transaction(recipient.getAccount(), source.getAccount(), amount, localTime, OperationType.Credit , transactionType));
    }
}
