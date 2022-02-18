package com.example.pyramid.facade;

import com.example.pyramid.model.Person;
import com.example.pyramid.services.api.BinaryTreeService;
import com.example.pyramid.services.api.RegisterTreeService;
import com.example.pyramid.services.api.TaxService;
import com.example.pyramid.services._BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@EnableScheduling
public class Facade extends _BaseService {

    @Autowired
    TaxService taxService;

    @Autowired
    RegisterTreeService registerTreeService;

    @Autowired
    BinaryTreeService bstService;

    public void registerUser(Person person, Long parentId) {
        registerTreeService.registerUser(person, parentId);
    }

    public void createAccount(Person person, BigDecimal amount) throws Exception {
        registerTreeService.createBankAccount(person, amount);
        taxService.payTax(person, amount);
    }

    @Scheduled(cron = "01 00 1 * * ?")
    public void groupBonus() {
        bstService.processGroupBonus();
    }
}
