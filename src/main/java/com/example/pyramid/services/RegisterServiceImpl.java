package com.example.pyramid.services;

import com.example.pyramid.DTO.PersonDTO;
import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.Person;
import com.example.pyramid.services.api.RegisterService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegisterServiceImpl extends _BaseService implements RegisterService {


    @Override
    public void registerUser(PersonDTO person, Long parentId) {
        Person newPerson = Person.builder()
                .setName(person.getName())
                .setParent(em.find(Person.class, parentId))
                .build();
        em.persist(newPerson);
    }

    @Override
    public void createBankAccount(Person person, BigDecimal amount) {
        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setBalance(amount);

        Person owner = em.createQuery("select p from Person p where p.name =: name", Person.class)
                .setParameter("name", person.getName())
                .getSingleResult();
        newBankAccount.setOwner(owner);
        em.persist(newBankAccount);
    }
}
