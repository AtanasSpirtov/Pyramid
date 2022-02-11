package com.example.pyramid.model;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Tax extends _BaseEntity{

    private String name;

    private BigDecimal taxAmount;

    private BigDecimal bonusPercentsInDirectBonus;

    private BigDecimal bonusPercentsInGroupBonus;

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

    public BigDecimal getBonusPercentsInDirectBonus() {
        return bonusPercentsInDirectBonus;
    }

    public void setBonusPercentsInDirectBonus(BigDecimal bonusPercentsInDirectBonus) {
        this.bonusPercentsInDirectBonus = bonusPercentsInDirectBonus;
    }

    public BigDecimal getBonusPercentsInGroupBonus() {
        return bonusPercentsInGroupBonus;
    }

    public void setBonusPercentsInGroupBonus(BigDecimal bonusPercentsInGroupBonus) {
        this.bonusPercentsInGroupBonus = bonusPercentsInGroupBonus;
    }
}
