package com.example.pyramid.services;

import com.example.pyramid.model.Person;
import com.example.pyramid.model.Tax;
import com.example.pyramid.services.api.BankService;
import com.example.pyramid.services.api.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TaxServiceImpl extends _BaseService implements TaxService {

    private static final Long COMPANY_ID = 1L;

    @Autowired
    BankService bankService;

    @Override
    public void payTax(Person person, BigDecimal amount) {
        List<Tax> taxes = em.createQuery("select tax from Tax tax", Tax.class).getResultList();

        taxes.sort((a, b) -> b.getTaxAmount().compareTo(a.getTaxAmount()));

        Tax t = taxes.stream().filter(tax -> amount.subtract(BigDecimal.valueOf(tax.getTaxAmount())).compareTo(BigDecimal.ZERO) >= 0)
                .findFirst().orElseThrow();

        List<Person> parents = parentChain(person).limit(3).collect(Collectors.toList());

        try {
            bankService.transferMoney(person, parents.get(0), BigDecimal.valueOf(t.getTaxAmount() % 5));
            bankService.transferMoney(person, parents.get(1), BigDecimal.valueOf(t.getTaxAmount() % 3));
            bankService.transferMoney(person, parents.get(2), BigDecimal.valueOf(t.getTaxAmount() % 2));
            bankService.transferMoney(person, em.find(Person.class, COMPANY_ID), BigDecimal.valueOf(t.getTaxAmount() * 0.9));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Stream<Person> parentChain(Person person) {
        return Objects.nonNull(person.getParent())
                ? Stream.concat(Stream.of(person.getParent()), parentChain(person.getParent())) : Stream.empty();
    }
}
