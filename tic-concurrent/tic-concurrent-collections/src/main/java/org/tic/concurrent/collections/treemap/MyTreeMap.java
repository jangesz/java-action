package org.tic.concurrent.collections.treemap;

import java.util.*;

public class MyTreeMap<K,V> extends AbstractMap<K,V>
        implements NavigableMap<K,V>, Cloneable, java.io.Serializable {

    private final Comparator<? super K> comparator;

    private transient Entry<K, V> root;

    private transient int size = 0;

    private transient int modCount = 0;

    public MyTreeMap() {
        this.comparator = null;
    }

    public MyTreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }


    // Red-black mechanics

    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    /**
     * Node in the Tree.  Doubles as a means to pass key-value pairs back to
     * user (see Map.Entry).
     */

    static final class Entry<K,V> implements Map.Entry<K,V> {
        K key;
        V value;
        Entry<K,V> left;
        Entry<K,V> right;
        Entry<K,V> parent;
        boolean color = BLACK;

        /**
         * Make a new cell with given key, value, and parent, and with
         * {@code null} child links, and BLACK color.
         */
        Entry(K key, V value, Entry<K,V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        /**
         * Returns the key.
         *
         * @return the key
         */
        public K getKey() {
            return key;
        }

        /**
         * Returns the value associated with the key.
         *
         * @return the value associated with the key
         */
        public V getValue() {
            return value;
        }

        /**
         * Replaces the value currently associated with the key with the given
         * value.
         *
         * @return the value associated with the key before this method was
         *         called
         */
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry))
                return false;
            Map.Entry<?,?> e = (Map.Entry<?,?>)o;

            return valEquals(key,e.getKey()) && valEquals(value,e.getValue());
        }

        public int hashCode() {
            int keyHash = (key==null ? 0 : key.hashCode());
            int valueHash = (value==null ? 0 : value.hashCode());
            return keyHash ^ valueHash;
        }

        public String toString() {
            return key + "=" + value;
        }
    }

    // Little utilities

    /**
     * Compares two keys using the correct comparison method for this TreeMap.
     */
    @SuppressWarnings("unchecked")
    final int compare(Object k1, Object k2) {
        return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
                : comparator.compare((K)k1, (K)k2);
    }

    /**
     * Test two values for equality.  Differs from o1.equals(o2) only in
     * that it copes with {@code null} o1 properly.
     */
    static final boolean valEquals(Object o1, Object o2) {
        return (o1==null ? o2==null : o1.equals(o2));
    }

    private static <K,V> boolean colorOf(Entry<K,V> p) {
        return (p == null ? BLACK : p.color);
    }

    private static <K,V> Entry<K,V> parentOf(Entry<K,V> p) {
        return (p == null ? null: p.parent);
    }

    private static <K,V> void setColor(Entry<K,V> p, boolean c) {
        if (p != null)
            p.color = c;
    }

    private static <K,V> Entry<K,V> leftOf(Entry<K,V> p) {
        return (p == null) ? null: p.left;
    }

    private static <K,V> Entry<K,V> rightOf(Entry<K,V> p) {
        return (p == null) ? null: p.right;
    }


    public V put(K key, V value) {
        Entry<K,V> t = root;
        // 如果根结点为空
        if (t == null) {
            // 检查是否key是否为null，TreeMap的key不能为null
            compare(key, key); // type (and possibly null) check

            // 创建一个根结点，返回null
            root = new Entry<>(key, value, null);
            size = 1;
            modCount++;
            return null;
        }
        // 记录比较结果
        int cmp;
        Entry<K,V> parent;
        // split comparator and comparable paths
        Comparator<? super K> cpr = comparator;
        // 如果comparator不为null
        if (cpr != null) {
            // 循环查找key要插入的位置
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);
            } while (t != null);
        }
        else {  // 如果comparator为null，则使用key作为比较器进行比较，并且key必须实现Comparable接口
            // 如果key为null，抛出NullPointException，TreeMap不允许key为null
            if (key == null)
                throw new NullPointerException();
            @SuppressWarnings("unchecked")
            Comparable<? super K> k = (Comparable<? super K>) key;
            // 循环查找key要插入的位置，如果没有找到key一致的，会一致遍历到叶子结点(NIL)，此时parent结点就是k准备要插入的父结点
            do {
                // t = root
                // 第一次循环，把root结点赋值给parent，然后循环检查记录父结点parent
                parent = t;
                cmp = k.compareTo(t.key);
                if (cmp < 0)
                    t = t.left;
                else if (cmp > 0)
                    t = t.right;
                else
                    return t.setValue(value);       // 如果遍历找到key一致的，直接覆盖value，并返回
            } while (t != null);
        }
        // 找到新结点的父结点(parent)后，创建结点对象
        Entry<K,V> e = new Entry<>(key, value, parent);
        // cmp是记录下来的比较结果，用于判断是插入在父结点的左侧还是右侧
        if (cmp < 0)    // 如果新结点key的值小于结点key的值，则插入在父结点的左侧
            parent.left = e;
        else    // 否则插入在父结点的右侧
            parent.right = e;
        // 插入新结点后，为了保持红黑树的特性，需要对红黑树进行调整
        fixAfterInsertion(e);
        size++;
        modCount++;
        // 如果插入的结点没有替换原来的结点，那么返回null
        return null;
    }

    /** From CLR */
    private void fixAfterInsertion(Entry<K,V> x) {
        // 将默认插入的结点设置成红色
        // 根据红黑树的性质（3），红黑树要求从根结点到叶子结点上经过的黑色结点个数是相同的
        // 因此，如果插入的结点设置为黑色，那必然有可能导致某条路径上的黑色结点数量大于其他路径上的黑色结点数量
        // 因此，默认插入的新结点都必须设置成红色，为此来维持红黑树的性质（3）
        x.color = RED;
        // 当把插入的结点设置为红色后，有可能导致违反性质（2），即出现连续两个红色结点，这就需要通过旋转操作去改变树的结构来解决这个问题。

        // 判断是否需要旋转的条件（当前结点不为NULL，且不是根结点，且它的父结点也是红色）这样就出现了连续两个红色结点的情况，需要旋转
        while (x != null && x != root && x.parent.color == RED) {
            // 当前结点的父结点 与 当前结点的父结点的父结点（即当前结点的祖父结点）的左子节点是否是同一个结点
            // 换句话说就是判断：当前结点是否左子节点插入
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                // 获取x结点的叔父结点，赋值给y
                Entry<K,V> y = rightOf(parentOf(parentOf(x)));
                // 如果叔父结点是红色的
                if (colorOf(y) == RED) {
                    // 把x结点的父结点设置为黑色
                    setColor(parentOf(x), BLACK);
                    // 把x结点的叔父结点设置为黑色
                    setColor(y, BLACK);
                    // 把x结点的祖父结点设置为红色
                    setColor(parentOf(parentOf(x)), RED);
                    // 把x赋值为其祖父结点，用于while条件重新判断
                    x = parentOf(parentOf(x));
                    // 保证不会连续出现两个红色结点
                } else {
                    // x是否为左子树内侧插入
                    if (x == rightOf(parentOf(x))) {
                        // 对x的父结点进行一次左旋
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    // 如果x是左子树外侧插入
                    // x的父结点设置为黑色
                    setColor(parentOf(x), BLACK);
                    // x的祖父结点设置为红色
                    setColor(parentOf(parentOf(x)), RED);
                    // 对x的祖父结点进行一次右旋
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Entry<K,V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    /**
     * From CLR
     * 左旋操作
     */
    private void rotateLeft(Entry<K,V> p) {
        // 旋转的结点不为null
        if (p != null) {
            // 记录 p 结点的右孩子为 r
            Entry<K,V> r = p.right;
            //
            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;
        }
    }

    /** From CLR */
    private void rotateRight(Entry<K,V> p) {
        if (p != null) {
            Entry<K,V> l = p.left;
            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public Entry<K, V> lowerEntry(K key) {
        return null;
    }

    @Override
    public K lowerKey(K key) {
        return null;
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        return null;
    }

    @Override
    public K floorKey(K key) {
        return null;
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        return null;
    }

    @Override
    public K ceilingKey(K key) {
        return null;
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        return null;
    }

    @Override
    public K higherKey(K key) {
        return null;
    }

    @Override
    public Entry<K, V> firstEntry() {
        return null;
    }

    @Override
    public Entry<K, V> lastEntry() {
        return null;
    }

    @Override
    public Entry<K, V> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<K, V> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<K, V> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<K> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<K> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super K> comparator() {
        return null;
    }

    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return null;
    }

    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return null;
    }

    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return null;
    }

    @Override
    public K firstKey() {
        return null;
    }

    @Override
    public K lastKey() {
        return null;
    }
}
