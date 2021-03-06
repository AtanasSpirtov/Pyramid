package com.example.pyramid.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import java.math.BigDecimal;

@Entity
@NamedQuery(name = "PartnerRank.FindPartnerRankForPerson", query = "select partnerRank from PartnerRank partnerRank "
        + "where partnerRank.minGroupBonus < :pAmount and partnerRank.count < :pChildrenCount order by partnerRank.minGroupBonus desc")

public class PartnerRank extends _BaseEntity{

    private String rank;

    private BigDecimal minGroupBonus;

    private int lineId;

    private int count;

    @Column(name = "generation_1")
    private BigDecimal generation1;

    @Column(name = "generation_2")
    private BigDecimal generation2;

    @Column(name = "generation_3")
    private BigDecimal generation3;

    @Column(name = "generation_4")
    private BigDecimal generation4;

    @Column(name = "generation_5")
    private BigDecimal generation5;

    @Column(name = "generation_6")
    private BigDecimal generation6;

    @Column(name = "generation_7")
    private BigDecimal generation7;

    public String getRank() {
        return rank;
    }

    public BigDecimal getMinGroupBonus() {
        return minGroupBonus;
    }

    public int getLineId() {
        return lineId;
    }

    public int getCount() {
        return count;
    }

    public BigDecimal getGeneration1() {
        return generation1;
    }

    public BigDecimal getGeneration2() {
        return generation2;
    }

    public BigDecimal getGeneration3() {
        return generation3;
    }

    public BigDecimal getGeneration4() {
        return generation4;
    }

    public BigDecimal getGeneration5() {
        return generation5;
    }

    public BigDecimal getGeneration6() {
        return generation6;
    }

    public BigDecimal getGeneration7() {
        return generation7;
    }
}
