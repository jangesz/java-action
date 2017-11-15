package org.tic.java8.lambda.function;

import java.util.function.Function;

public class JDK8FunctionTest {

    public static void main(String[] args) {

//        Function<Double, Double> sqrt = new Function<Double, Double>() {
//            @Override
//            public Double apply(Double aDouble) {
//                return Math.sqrt(aDouble);
//            }
//        };

        Function<Double, Double> sqrt = Math::sqrt;
        sqrt.apply(4.0);

    }

}
