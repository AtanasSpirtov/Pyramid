package com.example.pyramid.model;

import com.example.pyramid.model.enums.bstEnums.PositionInBinaryTree;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(indexes = {
        @Index(name = "registatorIndex" , columnList = "registrator") })
public class BinaryTreePerson extends _BaseEntity {

    @OneToOne
    Person person;

    @ManyToOne
    private BinaryTreePerson parent;

    @Enumerated(EnumType.STRING)
    private PositionInBinaryTree position;

    private BigDecimal leftBox = BigDecimal.ZERO;

    private BigDecimal midBox = BigDecimal.ZERO;

    private BigDecimal rightBox = BigDecimal.ZERO;

    @OneToOne
    private BinaryTreePerson registrator;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public BinaryTreePerson getParent() {
        return parent;
    }

    public void setParent(BinaryTreePerson parent) {
        this.parent = parent;
    }

    public PositionInBinaryTree getPosition() {
        return position;
    }

    public void setPosition(PositionInBinaryTree position) {
        this.position = position;
    }

    public BigDecimal getLeftBox() {
        return leftBox;
    }

    public void setLeftBox(BigDecimal leftBox) {
        this.leftBox = leftBox;
    }

    public BigDecimal getMidBox() {
        return midBox;
    }

    public void setMidBox(BigDecimal midBox) {
        this.midBox = midBox;
    }

    public BigDecimal getRightBox() {
        return rightBox;
    }

    public void setRightBox(BigDecimal rightBox) {
        this.rightBox = rightBox;
    }

    public BinaryTreePerson getRegistrator() {
        return registrator;
    }

    public void setRegistrator(BinaryTreePerson registrant) {
        this.registrator = registrant;
    }

}
