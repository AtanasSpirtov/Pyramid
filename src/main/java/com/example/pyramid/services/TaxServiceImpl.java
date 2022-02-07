package com.example.pyramid.services;

import com.example.pyramid.model.Tax;
import com.example.pyramid.services.api.TaxService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class TaxServiceImpl extends _BaseService implements TaxService {

    @Override
    public void payTax(Tax askedTax) {
        List<Tax> taxes = em.createQuery("select tax from Tax tax", Tax.class).getResultList();

        taxes.stream()
                .filter(tax -> Objects.equals(tax.getName(), askedTax.getName())
                        && Objects.equals(tax.getTaxAmount(), askedTax.getTaxAmount()))
                .findFirst().orElseThrow(NoSuchElementException::new);
    }
}
