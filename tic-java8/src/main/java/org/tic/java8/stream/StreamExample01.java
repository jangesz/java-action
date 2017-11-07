package org.tic.java8.stream;

import java.util.Comparator;
import java.util.stream.Stream;

public class StreamExample01 {

    public static void main(String[] args) {
        //of静态方法创建Stream并操作
        Stream.of("aaa1", "aaa2", "bbb2", "bbb1", "zzz3", "zzz31", "zzz21").sorted(Comparator.reverseOrder()).forEach(System.out::println);

        //generator方法，生成一个无限长度的Stream
        //这里会一直打印随机数
        //可以通过limit()来限制生成的数目
        Stream.generate(Math::random).limit(10).forEach(System.out::println);

        //iterate方法，生成一个无限长度的Stream，和generator不同的是，其元素的生成是重复对给定的种子值（seed）调用用户指定函数来生成的。
        Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::println);
    }

}
