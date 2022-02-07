package com.example.pyramid.model;

import javax.persistence.Entity;

@Entity
public class Tax extends _BaseEntity{

    private String name;

    private Double taxAmount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }
}
