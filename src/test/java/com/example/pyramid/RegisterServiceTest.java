package com.example.pyramid;

import com.example.pyramid.DTO.PersonDTO;
import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.Person;
import com.example.pyramid.services.api.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class RegisterServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    RegisterService registerService;

    @Test
    public void registerUserAndBankAccount() {

        //testing creation of user
        final PersonDTO personDTO = new PersonDTO();
        personDTO.setName("testUserCreation");
        final Person parent = em.find(Person.class , 2L);

        registerService.registerUser(personDTO , parent.getId());

        Person entity = em.createQuery("select p from Person p where p.name =: testName" , Person.class)
                .setParameter("testName", "testUserCreation")
                .getSingleResult();

        assertThat(entity).isNotNull();

        //testing creation of bank account
        registerService.createBankAccount(entity , BigDecimal.valueOf(1000));

        BankAccount account = em.createQuery("select b from BankAccount b where b.owner =: entity" , BankAccount.class)
                .setParameter("entity", entity)
                .getSingleResult();

        assertThat(account).isNotNull();

        em.remove(entity);
        em.remove(account);
    }
}

