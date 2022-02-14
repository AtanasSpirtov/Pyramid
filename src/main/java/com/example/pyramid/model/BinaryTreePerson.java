package com.example.pyramid.model;

import com.example.pyramid.model.enums.bstEnums.Box;
import com.example.pyramid.model.enums.bstEnums.PositionInBinaryTree;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.AbstractMap;

@Entity
public class BinaryTreePerson extends _BaseEntity {

    @OneToOne
    Person person;

    @ManyToOne
    private BinaryTreePerson parent;

    @Enumerated(EnumType.STRING)
    private PositionInBinaryTree position;

    AbstractMap.SimpleEntry<Box, BigDecimal> leftBox = new AbstractMap.SimpleEntry<>(Box.Left, BigDecimal.ZERO);

    AbstractMap.SimpleEntry<Box, BigDecimal> midBox = new AbstractMap.SimpleEntry<>(Box.Mid, BigDecimal.ZERO);

    AbstractMap.SimpleEntry<Box, BigDecimal> rightBox = new AbstractMap.SimpleEntry<>(Box.Right, BigDecimal.ZERO);


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

    public AbstractMap.SimpleEntry<Box, BigDecimal> getLeftBox() {
        return leftBox;
    }

    public void setLeftBox(AbstractMap.SimpleEntry<Box, BigDecimal> leftBox) {
        this.leftBox = leftBox;
    }

    public AbstractMap.SimpleEntry<Box, BigDecimal> getMidBox() {
        return midBox;
    }

    public void setMidBox(AbstractMap.SimpleEntry<Box, BigDecimal> midBox) {
        this.midBox = midBox;
    }

    public AbstractMap.SimpleEntry<Box, BigDecimal> getRightBox() {
        return rightBox;
    }

    public void setRightBox(AbstractMap.SimpleEntry<Box, BigDecimal> rightBox) {
        this.rightBox = rightBox;
    }
}
