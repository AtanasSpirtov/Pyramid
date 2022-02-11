package com.example.pyramid.services;

import com.example.pyramid.model.Person;
import com.example.pyramid.model.Tax;
import com.example.pyramid.model.enums.TransactionType;
import com.example.pyramid.services.api.BankService;
import com.example.pyramid.services.api.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.math.RoundingMode.*;

@Service
public class TaxServiceImpl extends _BaseService implements TaxService {

    private final BigDecimal GRAND_GRAND_FATHER_BONUS = BigDecimal.valueOf(2);

    private final BigDecimal GRAND_FATHER_BONUS = BigDecimal.valueOf(3);

    private final BigDecimal FATHER_BONUS = BigDecimal.valueOf(5);

    private static final BigDecimal BIG_DECIMAL_100 = BigDecimal.valueOf(100);

    private static final Long COMPANY_ID = 1L;

    @Autowired
    BankService bankService;

    @Override
    @Transactional
    public void payTax(Person person, BigDecimal amount) {
        Tax tax = em.createQuery(
                        "select tax from Tax tax where tax.taxAmount < :pAmount order by tax.taxAmount desc ", Tax.class)
                .setParameter("pAmount", amount).setMaxResults(1).getSingleResult();

        //mark tax as paid
        person.setTaxTypePaid(tax);
        person.setTaxExpirationDate(LocalDate.now().plusMonths(1L));

        Person company = em.find(Person.class, COMPANY_ID);
        Objects.requireNonNull(company, "Goljam fal: company is not found!");

        //return money to company
        BigDecimal taxAmount = tax.getTaxAmount().setScale(2 , FLOOR);
        bankService.transferMoney(person.getAccount(), company.getAccount(), taxAmount, TransactionType.Tax);

        // process direct bonus
        final Consumer<Person> personConsumer = parents -> {
            Iterator<BigDecimal> taxPercents =
                    Arrays.asList(GRAND_GRAND_FATHER_BONUS , GRAND_FATHER_BONUS , FATHER_BONUS).iterator();
            BigDecimal bonus = taxAmount.multiply(taxPercents.next()).divide(BIG_DECIMAL_100 , FLOOR).setScale(2, FLOOR);
            bankService.transferMoney(company.getAccount(), parents.getAccount(), bonus, TransactionType.DirectBonus);
        };

        parentChain(person).limit(3).forEach(personConsumer);

    }

    private static Stream<Person> parentChain(Person person) {
        return Objects.nonNull(person.getParent())
                ? Stream.concat(Stream.of(person.getParent()), parentChain(person.getParent())) : Stream.empty();
    }
}
