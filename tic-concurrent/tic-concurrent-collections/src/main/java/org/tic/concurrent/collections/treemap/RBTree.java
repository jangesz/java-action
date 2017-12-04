package org.tic.concurrent.collections.treemap;

public class RBTree<K, V> {


    private static final int RED = 1;
    private static final int BLACK = 2;

    static class Node<K, V> {
        Node<K, V> parent;
        Node<K, V> right;
        Node<K, V> left;
        int color = RED;

        public Node(Node right, Node left, Node parent, int color) {
            this.right = right;
            this.left = left;
            this.parent = parent;
            this.color = color;
        }



    }

}
