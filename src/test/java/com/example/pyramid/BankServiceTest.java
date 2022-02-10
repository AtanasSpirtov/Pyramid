package com.example.pyramid;

import com.example.pyramid.model.Person;
import com.example.pyramid.model.Transaction;
import com.example.pyramid.services.api.BankService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class BankServiceTest {

    @Autowired
    BankService bankService;

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    void transferMoney()
    {
        try {
            bankService.transferMoney(em.find(Person.class , 2L) , em.find(Person.class , 3L) , BigDecimal.valueOf(1000));

            Transaction source = em.createQuery("select t from Transaction t where t.sourceAccount.name =: TestCompany" , Transaction.class)
                    .setParameter("TestCompany", "TestCompany").getSingleResult();
            Transaction recipient = em.createQuery("select t from Transaction t where t.sourceAccount.name =: testPerson" , Transaction.class)
                    .setParameter("testPerson", "TestPerson").getSingleResult();

            assertThat(source).isNotNull();
            assertThat(recipient).isNotNull();
            em.remove(source);
            em.remove(recipient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
