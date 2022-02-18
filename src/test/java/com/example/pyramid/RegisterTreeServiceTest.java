package com.example.pyramid;

import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.Person;
import com.example.pyramid.services.api.RegisterTreeService;
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
public class RegisterTreeServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    RegisterTreeService registerTreeService;

    @Test
    public void registerUserAndBankAccount() {

        //testing creation of user
        final Person person = new Person();
        person.setName("testUserCreation");
        final Person parent = em.find(Person.class , 2L);

        registerTreeService.registerUser(person , parent.getId());

        Person entity = em.createQuery("select p from Person p where p.name =: testName" , Person.class)
                .setParameter("testName", "testUserCreation")
                .getSingleResult();

        assertThat(entity).isNotNull();

        //testing creation of bank account
        registerTreeService.createBankAccount(entity , BigDecimal.valueOf(1000000000));

        BankAccount account = em.createQuery("select b from BankAccount b where b.balance = 1000000000" , BankAccount.class)
                .getSingleResult();

        assertThat(account).isNotNull();

        em.remove(entity);
        em.remove(account);
    }
}

