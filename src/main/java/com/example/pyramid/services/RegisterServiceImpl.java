package com.example.pyramid.services;

import com.example.pyramid.DTO.PersonDTO;
import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.Person;
import com.example.pyramid.services.api.RegisterService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class RegisterServiceImpl extends _BaseService implements RegisterService {


    @Override
    public void registerUser(PersonDTO person, Long parentId) {
        Person newPerson = new Person();
        newPerson.setName(person.getName());
        newPerson.setParent(em.find(Person.class, parentId));
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
    }
}
