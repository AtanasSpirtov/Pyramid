package com.example.pyramid;

import com.example.pyramid.model.BankAccount;
import com.example.pyramid.model.Person;
import com.example.pyramid.model.Transaction;
import com.example.pyramid.model.enums.TransactionType;
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
            bankService.transferMoney(em.find(BankAccount.class , 2L) , em.find(BankAccount.class , 3L) , BigDecimal.valueOf(1000000000) , TransactionType.Tax);

            Transaction source = em.createQuery("select t from Transaction t where t.transactionAmount =: amount and t.operationType = 'Credit'" , Transaction.class)
                    .setParameter("amount" , BigDecimal.valueOf(1000000000)).getSingleResult();
            Transaction recipient = em.createQuery("select t from Transaction t where t.transactionAmount =: amount and t.operationType = 'Credit'" , Transaction.class)
                    .setParameter("amount" , BigDecimal.valueOf(1000000000)).getSingleResult();

            assertThat(source).isNotNull();
            assertThat(recipient).isNotNull();
            em.remove(source);
            em.remove(recipient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
