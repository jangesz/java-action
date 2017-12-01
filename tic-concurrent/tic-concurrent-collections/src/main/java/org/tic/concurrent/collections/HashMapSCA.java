package org.tic.concurrent.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * HashMap源码分析，SCA = Source Code Analysis
 *
 * 哈希数组（哈希表），哈希表的每个元素都是一个单链表的头节点，链表用来解决冲突，
 *
 *
 * HashMap有一个静态内部类Entry，用来存储key、value、next。哈希数组就是一个Entry[]。
 *
 */
public class HashMapSCA {

//    public static void main(String[] args) {
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("abc", "ABC");
//        int i  = 34 + 16 * 10000;
//        i = i ^ (i>>>16);
//        System.out.println(Integer.toBinaryString(i));
//        System.out.println(Integer.toBinaryString(34 + 16*10000));
//
//        // tableForSize 学习
//        int cap = 16 - 1;
//        System.out.println(formatToString(cap));
//
//        System.out.println("------------------------------------------------------------");
//        System.out.println("after >>> 1");
//        System.out.println(formatToString(cap >>> 1));
//        cap |= cap >>> 1;
//        System.out.println("after >>> 1 and |= return value is:");
//        System.out.println(formatToString(cap));
//
//        System.out.println("------------------------------------------------------------");
//        System.out.println("after >>> 2");
//        System.out.println(formatToString(cap >>> 2));
//        cap |= cap >>> 2;
//        System.out.println("after >>> 2 and |= return value is:");
//        System.out.println(formatToString(cap));
//
//        System.out.println("------------------------------------------------------------");
//        System.out.println("after >>> 4");
//        System.out.println(formatToString(cap >>> 4));
//        cap |= cap >>> 4;
//        System.out.println("after >>> 4 and |= return value is:");
//        System.out.println(formatToString(cap));
//
//        System.out.println("------------------------------------------------------------");
//        System.out.println("after >>> 8");
//        System.out.println(formatToString(cap >>> 8));
//        cap |= cap >>> 8;
//        System.out.println("after >>> 8 and |= return value is:");
//        System.out.println(formatToString(cap));
//
//        System.out.println("------------------------------------------------------------");
//        System.out.println("after >>> 16");
//        System.out.println(formatToString(cap >>> 16));
//        cap |= cap >>> 16;
//        System.out.println("after >>> 16 and |= return value is:");
//        System.out.println(formatToString(cap));
//
//    }

    private static String formatToString(int i) {
        return String.format("%032d", Integer.parseInt(Integer.toBinaryString(i)));
    }

    /*
     *  二进制
     *  byte（整型）：8位
     *  short（整型）：16位
     *  char（字符型）：16位
     *  int（整型）：32位
     *  float（浮点型单精度）：32位
     *  long（整型）：64位
     *  double（浮点型双精度）：64位
     */

    public static void main(String[] args) {
        HashA a = new HashA("hashA");
        HashB b = new HashB("hashB");

        HashMap<Hash, Hash> hashMap = new HashMap<>();
        hashMap.put(a, a);
        System.out.println(hashMap.get(a).getName());

        hashMap.put(b,b);
        System.out.println(hashMap.get(a).getName());
        System.out.println(hashMap.get(b).getName());

        Iterator<Map.Entry<Hash, Hash>> iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }

        Iterator<Hash> keyiter = hashMap.keySet().iterator();
        while (keyiter.hasNext()) {
            System.out.println(keyiter.next().getName());
        }
    }

    static class Hash{
        public String getName(){
            return "";
        }
    }

    static class HashA extends Hash{
        private String name;

        public HashA(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            return 2;
        }
    }

    static class HashB extends Hash{
        private String name;

        public HashB(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            return 2;
//            return 160034;
        }
    }
}
