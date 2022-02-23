package com.example.pyramid.services;

import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.PartnerRank;
import com.example.pyramid.model.Person;
import com.example.pyramid.model.enums.TransactionType;
import com.example.pyramid.services.api.BankService;
import com.example.pyramid.services.api.RegisterTreeService;
import com.example.pyramid.utils.Calculator;
import com.example.pyramid.utils.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RegisterTreeServiceImpl extends _BaseService implements RegisterTreeService {

    @Autowired
    BankService bankService;

    @Override
    public void registerUser(Person person, Long parentId) {
        Person newPerson = Person.builder()
                .setName(person.getName())
                .setParent(em.find(Person.class, parentId))
                .setRegistrationDate(LocalDate.now())
                .build();

        em.persist(newPerson);
    }

    @Override
    public void createBankAccount(Person person, BigDecimal amount) {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setBalance(amount);
        em.persist(newBankAccount);
    }
}
