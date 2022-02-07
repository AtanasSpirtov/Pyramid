package com.example.pyramid.model;

import javax.persistence.Entity;

@Entity
public class Tax extends _BaseEntity{

    private String name;

    private Long taxAmount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Long taxAmount) {
        this.taxAmount = taxAmount;
    }
}
