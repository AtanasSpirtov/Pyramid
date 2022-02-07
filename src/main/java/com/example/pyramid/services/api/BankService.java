package com.example.pyramid.services.api;

import com.example.pyramid.model.Person;

import java.math.BigDecimal;

public interface BankService{
    void transferMoney(Person source, Person recipient , BigDecimal amount) throws Exception;
}
