package org.tic.java8.stream;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamFlatMapExample01 {

    //http://blog.csdn.net/johnny901114/article/details/51532776
    //http://www.mkyong.com/java8/java-8-flatmap-example/
    //http://ifeve.com/stream/
    //https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/
    public static void main(String[] args) {

        int[] intArray = {1, 2, 3, 4, 5, 6};

        Stream<int[]> streamArray = Stream.of(intArray);

        IntStream intStream = streamArray.flatMapToInt(Arrays::stream);

        intStream.forEach(System.out::println);

    }

}
