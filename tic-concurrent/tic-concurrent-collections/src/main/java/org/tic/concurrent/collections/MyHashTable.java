package org.tic.concurrent.collections;

import java.util.Hashtable;
import java.util.LinkedHashMap;

public class MyHashTable {

    /**
     *
     * HashMap、Hashtable确定数组索引的方式不同：
     *
     * HashMap：i = (n - 1) & hash
     * Hashtable：index = (hash & 0x7FFFFFFF) % tab.length
     *
     *
     */
    public static void main(String[] args) {
        Hashtable ht = new Hashtable();
        LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
    }

}
