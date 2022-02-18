package com.example.pyramid;

import com.example.pyramid.model.Person;
import com.example.pyramid.services.api.RegisterTreeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
public class TestRegisterTree {
    @Autowired
    RegisterTreeService reg;

    @PersistenceContext
    EntityManager em;

}
