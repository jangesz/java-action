package org.tic.java8.lambda.function;

import java.util.function.Function;

public class SqrtFunction implements Function<Double, Double> {
    @Override
    public Double apply(Double aDouble) {
        return Math.sqrt(aDouble);
    }

    public static void main(String[] args) {
        System.out.println(new SqrtFunction().apply(4.0));
    }

}
