package com.example.pyramid.services;

import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.Person;
import com.example.pyramid.services.api.RegisterService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class RegisterServiceImpl extends _BaseService implements RegisterService {

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
