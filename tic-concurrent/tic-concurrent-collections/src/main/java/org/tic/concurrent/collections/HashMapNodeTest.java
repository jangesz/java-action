package org.tic.concurrent.collections;

import java.util.Map;
import java.util.Objects;

public class HashMapNodeTest {

    public static void main(String[] args) {
        TestHashMap<String, String> testHashMap = new TestHashMap<>();
        testHashMap.put("hello", "world");
    }

    private static class TestHashMap<K, V> {

        public V put(K key, V value) {
            Node<K,V> p = newNode(2, key, value, null);
            Node<K,V> e;

            if ((e = p.next) == null) {
                // 新建一个结点，p的next指针指向该结点
                p.next = newNode(3, key, value, null);
            }

            System.out.println(p.getKey() + " : " + p.getValue());
            System.out.println(p.next.getKey() + " - " + p.next.getValue());
            System.out.println(e);
            if (e!=null) {
                System.out.println(e.getKey() + " : " + e.getValue());
            }

            return value;
        }

        // Create a regular (non-tree) node
        private Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
            return new Node<>(hash, key, value, next);
        }

        private class Node<K,V> implements Map.Entry<K,V> {
            final int hash;
            final K key;
            V value;
            Node<K,V> next;

            Node(int hash, K key, V value, Node<K,V> next) {
                this.hash = hash;
                this.key = key;
                this.value = value;
                this.next = next;
            }

            public final K getKey()        { return key; }
            public final V getValue()      { return value; }
            public final String toString() { return key + "=" + value; }

            public final int hashCode() {
                return Objects.hashCode(key) ^ Objects.hashCode(value);
            }

            public final V setValue(V newValue) {
                V oldValue = value;
                value = newValue;
                return oldValue;
            }

            public final boolean equals(Object o) {
                if (o == this)
                    return true;
                if (o instanceof Map.Entry) {
                    Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                    if (Objects.equals(key, e.getKey()) &&
                            Objects.equals(value, e.getValue()))
                        return true;
                }
                return false;
            }
        }
    }






}
