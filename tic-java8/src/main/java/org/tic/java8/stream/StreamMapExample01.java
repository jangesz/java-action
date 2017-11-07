package org.tic.java8.stream;

import java.util.ArrayList;
import java.util.List;

public class StreamMapExample01 {

    private static List<String> stringList = new ArrayList<>();

    /*
    * 1. stream()方法是Collection接口的一个默认方法
    * 2. filter方法参数是一个Predicate函数式接口并继续返回Stream接口
    * 3. forEach方法参数是一个Consumer函数式接口
    */

    public static void main(String[] args) {
        initData();

        //test01
        System.out.println("---------------------------test01---------------------------");
        stringList.stream().filter(s -> s.startsWith("a")).forEach(System.out::println);

        //test02
        System.out.println("---------------------------test02---------------------------");
        stringList.stream().sorted().filter(s -> s.startsWith("a")).forEach(System.out::println);
        System.out.println("---------------------------test02|输出原始集合数据---------------------------");
        stringList.forEach(System.out::println);


        System.out.println("---------------------------test03---------------------------");
        //map
        //map是一个流对象的中间操作，通过给定的方法，它能够把流对象中的每一个元素对应到另外一个对象上
        stringList.stream().map(String::toUpperCase).sorted((a,b) -> b.compareTo(a)).forEach(System.out::println);

    }

    private static void initData() {
        stringList.add("zzz1");
        stringList.add("aaa2");
        stringList.add("bbb2");
        stringList.add("fff1");
        stringList.add("fff2");
        stringList.add("aaa1");
        stringList.add("bbb1");
        stringList.add("zzz2");
    }


}
