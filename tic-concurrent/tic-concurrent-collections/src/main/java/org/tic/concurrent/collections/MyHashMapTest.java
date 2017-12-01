package org.tic.concurrent.collections;

import java.util.Map;

public class MyHashMapTest {

    public static void main(String[] args) {
        Map<String, String> map = new MyHashMap<>();
        map.put("abc", "ABC");
        System.out.println(map.get("abc"));
    }

}
