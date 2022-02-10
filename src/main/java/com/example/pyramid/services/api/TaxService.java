package com.example.pyramid.services.api;

import com.example.pyramid.model.Person;

import java.math.BigDecimal;

public interface TaxService {
    void payTax(Person person, BigDecimal amount) throws Exception;
}
