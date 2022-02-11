package com.example.pyramid.utils;

import java.math.BigDecimal;

public class Calculator {
    public static boolean isPositive(BigDecimal number) {
        return number.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNegative(BigDecimal number) {
        return number.compareTo(BigDecimal.ZERO) < 0;
    }

    public static int compare(BigDecimal numberOne, BigDecimal numberTwo) {
        return numberOne.compareTo(numberTwo);
    }

    public static BigDecimal subtractFromFirst(BigDecimal numberOne, BigDecimal numberTwo) {
        return numberOne.subtract(numberTwo);
    }

    public static BigDecimal addToFirst(BigDecimal numberOne, BigDecimal numberTwo) {
        return numberOne.add(numberTwo);
    }
}
