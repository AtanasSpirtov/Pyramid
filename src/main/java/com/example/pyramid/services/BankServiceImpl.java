package com.example.pyramid.services;

import com.example.pyramid.model.Person;
import com.example.pyramid.model.Transaction;
import com.example.pyramid.model.TransactionType;
import com.example.pyramid.services.api.BankService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

@Service
public class BankServiceImpl extends _BaseService implements BankService {

    private static final Long companyClearingAccount = -1L;

    @Override
    @Transactional
    public void transferMoney(Person source, Person recipient, BigDecimal amount) throws Exception {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("amount cannot be negative");
        }

        Objects.requireNonNull(source, "not existing user");
        Objects.requireNonNull(recipient, "not existing user");

        if (!source.getId().equals(companyClearingAccount)) {
            if (source.getAccount().getBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new Exception("balance cannot be negative");
            }
            if (source.getAccount().getBalance().compareTo(amount) < 0) {
                throw new Exception("amount cannot be bigger than account balance");
            }
        }

        LocalTime localTime = LocalTime.now();
        em.persist(new Transaction(source, recipient, amount, localTime, TransactionType.Debit));
        em.persist(new Transaction(recipient, source, amount, localTime, TransactionType.Credit));
    }
}
