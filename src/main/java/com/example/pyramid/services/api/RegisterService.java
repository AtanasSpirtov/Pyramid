package com.example.pyramid.services.api;

import com.example.pyramid.model.Person;

import java.math.BigDecimal;

public interface RegisterService {
    void registerUser(Person user , Long parentId);
    void createBankAccount(Person user , BigDecimal amount);
}
