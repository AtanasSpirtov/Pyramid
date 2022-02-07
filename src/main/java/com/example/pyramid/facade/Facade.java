package com.example.pyramid.facade;

import com.example.pyramid.DTO.PersonDTO;
import com.example.pyramid.model.Person;
import com.example.pyramid.model.Tax;
import com.example.pyramid.services.api.RegisterService;
import com.example.pyramid.services.api.TaxService;
import com.example.pyramid.services._BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class Facade extends _BaseService {

    @Autowired
    TaxService taxService;

    @Autowired
    RegisterService registerService;

    public void registerUser(PersonDTO person, Long parentId) {
        registerService.registerUser(person, parentId);
    }

    public void createAccount(Person person, BigDecimal amount) {
        registerService.createBankAccount(person , amount);
        taxService.payTax(person , amount);
    }
}
