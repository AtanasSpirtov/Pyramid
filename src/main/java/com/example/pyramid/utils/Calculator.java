package com.example.pyramid.utils;

import java.math.BigDecimal;

import static java.math.RoundingMode.FLOOR;

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

    public static BigDecimal subtractFromFirst(BigDecimal numberOne, BigDecimal numberTwo) {return numberOne.subtract(numberTwo);}

    /**
     * vfsklgsf dlkg;jsfgl; sf;lg s
     * @param numberOne
     * @param numberTwo
     * @return
     */
    public static BigDecimal addToFirst(BigDecimal numberOne, BigDecimal numberTwo) {
        return numberOne.add(numberTwo);
    }

    public static boolean isZero(BigDecimal number) {
        return number.equals(BigDecimal.ZERO);
    }
    public static BigDecimal findPercent(BigDecimal percentOfNumber , BigDecimal number){
        return number.multiply(percentOfNumber).divide(Properties.BIG_DECIMAL_100 , FLOOR).setScale(2 , FLOOR);
    }
}
