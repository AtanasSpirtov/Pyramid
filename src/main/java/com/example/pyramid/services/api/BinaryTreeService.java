package com.example.pyramid.services.api;

import com.example.pyramid.model.BinaryTreePerson;
import com.example.pyramid.model.Person;
import com.example.pyramid.model.enums.bstEnums.PositionInBinaryTree;

import java.math.BigDecimal;

public interface BinaryTreeService {
    void positionInBinaryTree(Person person, BinaryTreePerson parent, PositionInBinaryTree position);

    void addAmount(Person person, BigDecimal amount);

    void processGroupBonus();
}
