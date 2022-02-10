package com.example.pyramid.model;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Tax extends _BaseEntity{

    private String name;

    private BigDecimal taxAmount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}
