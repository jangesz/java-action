//package org.tic.concurrent.collections;
//
//import java.io.Serializable;
//import java.util.*;
//
///**
// * 参考文章：
// *  http://blog.csdn.net/qazwyc/article/details/76686915
// *  http://www.importnew.com/20386.html
// *  https://www.cnblogs.com/leesf456/p/5242233.html
// *  https://www.cnblogs.com/ITtangtang/p/3948406.html
// *
// *  https://www.cnblogs.com/fangfan/p/4086662.html
// *  http://blog.csdn.net/weixin_33547926/article/details/52385773
// *  http://blog.csdn.net/lyt_7cs1dn9/article/details/54925837
// *
// *  http://www.importnew.com/15230.html
// *
// *  HashMap特点：
// *      1. 基于Map接口实现
// *      2. 允许Null键/值
// *      3. 线程不安全
// *      4. 不保证有序（比如插入的顺序）、也不保证顺序不随时间变化
// *      5. 只支持浅拷贝，通过实现Cloneable接口
// *      6. java8的hash计算是通过key的hashCode()的高16位异或低16位实现的，既保证高位bit都能参与到hash的计算中，又不会有太大的开销
// *      7. java8的hashMap，引入了红黑树，当链表长度达到8，执行treeifyBin，当桶数量达到64 时，将链表转为红黑树，否则，执行resize
// *      8. 迭代器是fail-fast的，迭代器创建后如果进行了结构修改（增加或删除一个映射，覆盖原来的值不算结构修改）且不是使用的iterator的
// *      remove方法，会努力抛出ConcurrentModificationException，所以不能依赖该异常保证程序运行正确，而只可用于检测BUG。fail-fast是
// *      通过modCount来实现的。
// *
// *      //TODO 这个modCount在JDK8中不是volatile的，所以需要再深入研究下
// *      modCount修饰为volatile，保证线程之间修改的可见性。JDK不保证fail-fast机制一定会发生，所以这个仅仅只是
// *      用来检测BUG的一种方式
// *
// *  HashMap数据结构：
// *      数组 + 链表 + 红黑树
// *
// *  HashMap的技巧：
// *      当length 总是 2 的 n 次方时，h & (length - 1) 运算等价于对 length 取模，也就是 h % length，
// *      但这个比 h % length 更加效率（& 比 % 具有更高的效率）
// *
// *  HashMap的两种遍历方式：
// *      Iterator iter = map.entrySet().iterator();
// *      while(iter.hasNext()) {
// *          Map.Entry entry = (Map.Entry) iter.next;
// *          Object key = entry.getKey();
// *          Object val = entry.getValue();
// *      }           // 该迭代方法效率高
// *
// *      Iterator iter = map.keySet().iterator();
// *      while( iter.hasNext() ) {
// *          Object key = iter.next();
// *          Object val = map.get(key);
// *      }           // 该迭代方法效率低（相对），尽量少用
// *
// *
// * @param <K>
// * @param <V>
// */
//public class MyHashMap<K,V> extends AbstractMap<K,V>
//        implements Map<K,V>, Cloneable, Serializable {
//    // 序列号
//    private static final long serialVersionUID = 362498820763181265L;
//
//    // 默认的初始容量是16
//    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
//
//    // 最大容量
//    static final int MAXIMUM_CAPACITY = 1 << 30;
//
//    // 默认的填充因子
//    static final float DEFAULT_LOAD_FACTOR = 0.75f;
//
//    // 当桶（bucket）上的结点数大于这个值时会转成红黑树
//    static final int TREEIFY_THRESHOLD = 8;
//
//    // 当桶（bucket）上的结点数小于这个值时红黑树转成链表
//    static final int UNTREEIFY_THRESHOLD = 6;
//
//    // 桶中结构转化为红黑树对应的table的最小大小
//    static final int MIN_TREEIFY_CAPACITY = 64;
//
//    // 存储元素的数组，总是2的幂次倍
//    transient Node<K,V>[] table;
//
//    // 存放具体的元素的集
//    transient Set<Map.Entry<K, V>> entrySet;
//
//    // 存放元素的个数，注意这个不等于数组的长度
//    transient int size;
//
//    // 每次扩容和更改map结构的计数器
//    transient int modCount;
//
//    // 临界值，当实际大小（容量*填充银子）超过临界值时，会进行扩容
//    int threshold;
//
//    // 填充因子
//    final float loadFactor;
//
//    // Node结点，就是哈希数组中存放的元素，哈希数组也称为table。每个table元素存放一个Entry的对象，在HashMap中用Node来实现Map.Entry
//    // 可以看到HashMap有一个成员变量：transient Node<K,V>[] table，这个就是哈希数组，存放Node元素
//    // Node结点包括：Key、Value、以及一个指向下个结点的指针
//    static class Node<K,V> implements Map.Entry<K,V> {
//        final int hash;
//        final K key;
//        V value;
//        Node<K,V> next;
//
//        Node(int hash, K key, V value, Node<K,V> next) {
//            this.hash = hash;
//            this.key = key;
//            this.value = value;
//            this.next = next;
//        }
//
//        public final K getKey()        { return key; }
//        public final V getValue()      { return value; }
//        public final String toString() { return key + "=" + value; }
//
//        public final int hashCode() {
//            return Objects.hashCode(key) ^ Objects.hashCode(value);
//        }
//
//        public final V setValue(V newValue) {
//            V oldValue = value;
//            value = newValue;
//            return oldValue;
//        }
//
//        public final boolean equals(Object o) {
//            if (o == this)
//                return true;
//            if (o instanceof Map.Entry) {
//                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
//                if (Objects.equals(key, e.getKey()) &&
//                        Objects.equals(value, e.getValue()))
//                    return true;
//            }
//            return false;
//        }
//    }
//
//    public MyHashMap(int initialCapacity, float loadFactor) {
//        // 初始容量不能小于0，否则报错
//        if (initialCapacity < 0)
//            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
//
//        // 初始容量不能大于最大值，否则为最大值
//        if (initialCapacity > MAXIMUM_CAPACITY) {
//            initialCapacity = MAXIMUM_CAPACITY;
//        }
//
//        //填充银子不能小于或等于0，不能为非数字
//        if (loadFactor <=0 || Float.isNaN(loadFactor)) {
//            throw new IllegalArgumentException("Illegal load factory: " + loadFactor);
//        }
//
//        // 初始化填充因子
//        this.loadFactor = loadFactor;
//        // 初始化threshold大小
//        this.threshold = tableSizeFor(initialCapacity);
//    }
//
//    public MyHashMap(int initialCapacity) {
//        this(initialCapacity, DEFAULT_LOAD_FACTOR);
//    }
//
//    public MyHashMap() {
//        this.loadFactor = DEFAULT_LOAD_FACTOR;
//    }
//
//    /**
//     * Returns a power of two size for the given target capacity.
//     * 对于给定的目标容量cap，返回一个大于该cap的最小的二次幂数
//     *
//     * 算法：
//     *  >>> 是无符号右移（高位补0），假设从右往左第一个非0位为最高位
//     *  |= 位或操作，举例：0011 | 1100 = 1111，0001 | 1000 = 1001
//     *
//     * 算法流程举例（假设cap = 16）
//     * cap
//     * 00000000000000000000000000010000
//     * n
//     * 00000000000000000000000000001111
//     * 00000000000000000000000000000111        >>> 1
//     * 00000000000000000000000000001111        |=
//     * 00000000000000000000000000000011        >>> 2
//     * 00000000000000000000000000001111        |=
//     * 00000000000000000000000000000000        >>> 4
//     * 00000000000000000000000000001111        |=
//     * 00000000000000000000000000000000        >>> 8
//     * 00000000000000000000000000001111        |=
//     * 00000000000000000000000000000000        >>> 16
//     * 00000000000000000000000000001111        |=
//     *
//     * 最终 n = 15
//     * n + 1 =16，就是最终的tableSize，哈希数组的大小
//     */
//    static final int tableSizeFor(int cap) {
//        // 为什么要cap - 1？目的是要找到目标值大于或者等于原值的最小二次幂
//        // 举个例子：比如cap = 8，那如果不减1，下面的位移+位或计算后结果是 15，最后再加 1 = 16，16也是一个二次幂值，只是不等于8，所以不符合（最小）这个要求
//        // 所以需要减1，目的就是为了有可能找到的是“等于”原值的最小二次幂
//        // 综上所述，tableSizeFor首先是要保证找到的数是一个二次幂数，其次是要保证这个数是大于或者等于原值的一个二次幂数
//        int n = cap - 1;
//
//        // 以下（位移 >>> 和 位或 |= ）的功能，是让 n 这个数的最高位的 1 右边的位全部变为 1
//        // 这样操作过后，只要 + 1，那么就是大于 n 这个数的最小二次幂数
//        n |= n >>> 1;
//        n |= n >>> 2;
//        n |= n >>> 4;
//        n |= n >>> 8;
//        n |= n >>> 16;
//
//        // 判断计算出来的最小二次幂值，如果大于最大容量，直接取最大容量
//        // 反之，则 + 1 变成最小二次幂值
//        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
//    }
//
//    /**
//     *  没有直接使用key的hashCode()，而是使key的hashCode() 高16位不变，低16位与高16位异或作为最终hash值，那么为什么要这么做呢？
//     *  换句话说，为什么要让高位bit参与到hash的计算中呢？
//     *
//     *  须知：HashMap计算数组下标的方法是通过 hash & (n - 1)，没有使用取余操作（hash % n)，首先是因为逻辑与的效率大大高于取余运算，
//     *  其次由于HashMap的容量始终是一个二次幂数，所以可以通过逻辑与的取巧方法来替换取余运算。
//     *
//     *  举个例子：当前HashMap的容量是16，那么 hash & (n - 1)，就是hash & 15（0x1111），散列值真正生效的只有低 4 位。当新增的键的
//     *  hashCode()是2、18、34 这样恰好以16的倍数为差的等差数列时，就产生了大量碰撞。
//     *  2（0x10）& 15 (00000000000000000000000000001111)  = 0x10 = 2
//     *  18（0x10010）& 15 (00000000000000000000000000001111)  = 0x10 = 2
//     *  34（0x100010）& 15 (00000000000000000000000000001111)  = 0x10 = 2
//     *  ......
//     *  160034（100111000100100010）& 15 (00000000000000000000000000001111) = 0x10 = 2
//     *  因为16这个容量，只有低 4 位才会与散列值计算数组下标，高位都没有实际意义。
//     *  因此，设计者综合考虑了速度、作用、质量，把高16 bit 和低16 bit进行了异或运算。因为现在大多数的hashCode的分布已经很不错了，
//     *  就算是发生了较多碰撞也用0（logn）的红黑树去优化了。仅仅异或一下，既减少了系统的开销，也不会造成因为高位没有参与下标的计算
//     *  （table长度比较小时），从而引起的碰撞。还是以2、18、34 来举例如下：
//     *
//     *  2（0x10）异或运算后 = 0x10 & 15 = 0x10 = 2
//     *  18（0x10010）异或预算后 = 0x10010 & 15 = 0x10 = 2
//     *  34（0x100010）异或运算后 = 0x100010 & 15 = 0x10 = 2
//     *  ......
//     *  160034（100111000100100010） 异或运算后 = 0x100111000100100000 & 15 = 0x00 = 0
//     *            0000000000000010
//     *
//     *  putVal中的(n - 1) & hash 相当于 JDK1.7中的indexFor，通过hash值和数组长度来确定数组的下标，也就是确定该key/value存放在数组中的位置。
//     *
//     * @param key 键对象
//     * @return hash值
//     */
//    static final int hash(Object key) {
//        int h;
//        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
//    }
//
//    public V get(Object key) {
//        Node<K,V> e;
//        return (e = getNode(hash(key), key)) == null ? null : e.value;
//    }
//
//    /**
//     * Implements Map.get and related methods
//     *
//     * @param hash hash for key
//     * @param key the key
//     * @return the node, or null if none
//     */
//    final Node<K,V> getNode(int hash, Object key) {
//        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
//        if ((tab = table) != null && (n = tab.length) > 0 &&
//                (first = tab[(n - 1) & hash]) != null) {
//            if (first.hash == hash && // always check first node
//                    ((k = first.key) == key || (key != null && key.equals(k))))
//                return first;
//            if ((e = first.next) != null) {
//                if (first instanceof TreeNode)
//                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
//                do {
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k))))
//                        return e;
//                } while ((e = e.next) != null);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public V put(K key, V value) {
//        return putVal(hash(key), key, value, false, true);
//    }
//
//    // final方法，默认修饰符default，同一包下方法可见
//    // 申明final方法，也就不让子类去覆盖
//    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
//                   boolean evict) {
//        Node<K,V>[] tab; Node<K,V> p; int n, i;
//        // table未初始化或者长度为0，进行扩容
//        if ((tab = table) == null || (n = tab.length) == 0)
//            n = (tab = resize()).length;
//        // (n-1) & hash，计算数组下标（即确定元素存放在哪个桶中），桶为空（即对null处理），新生成结点放入桶中（此时，这个结点是放在数组中）
//        // 此时第一个结点的next指针 = null
//        if ((p = tab[i = (n - 1) & hash]) == null)
//            tab[i] = newNode(hash, key, value, null);
//        // 桶中已经有元素
//        else {
//            Node<K,V> e; K k;
//            // 比较桶中第一个元素（数组中的节点）的hash值相等，key相等，直接覆盖value
//            if (p.hash == hash &&
//                    ((k = p.key) == key || (key != null && key.equals(k))))
//                // 将第一个元素赋值给e，用e来记录，准备覆盖value，下面会覆盖
//                e = p;
//            // hash值不相等，即key不相等，并且判断p结点（桶中的结点）是否是一颗红黑树
//            // 如果是红黑树，则通过红黑树的方法，把该结点插入
//            else if (p instanceof TreeNode)
//                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
//            // key不相等，也不是红黑树，就是链表
//            else {
//                // 遍历链表，直到找到最后一个结点（即p.next = null)
//                for (int binCount = 0; ; ++binCount) {
//                    if ((e = p.next) == null) {
//                        // 新建一个结点，p的next指针指向该结点
//                        p.next = newNode(hash, key, value, null);
//                        // 判断链表长度是否 > 8 进行红黑树转换
//                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
//                            // 转换成红黑树
//                            treeifyBin(tab, hash);
//                        // 当已经遍历到最后一个结点，就break，退出循环
//                        // 退出该死循环的条件是p.next == null，而if ((e = p.next) == null)会先对e进行赋值
//                        // 所以如果e == null，说明没有找到key hash一样的结点，需要新增该结点，插入到该链表中，或者红黑树中
//                        // 如果 e != null，说明找到了key hash一样的结点，只要覆盖value就行，具体代码在下面 if (e != null) // existing mapping for key
//                        // 或者遍历到的结点中有hash key相同的，如下的if (e.hash == hash && ......)
//                        break;
//                    }
//                    // key已经存在，则直接退出循环，后面做value覆盖
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k))))
//                        break;
//                    // 如果当前遍历到的结点的next != null，把当前结点赋值给 p 变量，再次遍历判断p.next == null
//                    // 直到找到最后一个结点
//                    p = e;
//                }
//            }
//
//            // 覆盖value，e结点就是table中已经存在的结点，并且与key的hash值一致
//            if (e != null) { // existing mapping for key
//                // 获取旧值，记录下来
//                V oldValue = e.value;
//                // onlyIfAbsent = false，所以这里肯定是true，直接覆盖
//                if (!onlyIfAbsent || oldValue == null)
//                    // 用新值替换旧值
//                    e.value = value;
//                // 访问后回调
//                afterNodeAccess(e);
//                // 返回旧值
//                return oldValue;
//            }
//        }
//
//        // 结构性修改
//        ++modCount;
//        // 新增一个结点后，如果超过最大容量，则扩容
//        if (++size > threshold)
//            resize();
//
//        // 插入后回调
//        afterNodeInsertion(evict);
//        return null;
//    }
//
//    /**
//     * 扩容resize
//     * 1. 当数组未初始化，按照之前在threashold中保存的初始容量分配内存，没有就使用缺省值
//     * 2. 当超过限制时，就扩充两倍，因为我们使用的是2次幂的扩展，所以，元素的位置要么是原来的位置，要么是原位置再移动2次幂的位置（原位置 + 原来容量）
//     *
//     * 因此，我们在resize的时候，不需要重新计算hash，只需要看原来的hash值高位新增的那个bit位是 1 还是 0
//     */
//    final Node<K,V>[] resize() {
//        // 保存当前table
//        Node<K,V>[] oldTab = table;
//        // 保存当前table大小
//        int oldCap = (oldTab == null) ? 0 : oldTab.length;
//        // 保存当前阈值
//        int oldThr = threshold;
//
//        int newCap, newThr = 0;
//        // 如果之前table.length > 0
//        if (oldCap > 0) {
//            // 判断之前table.length 是否大于最大容量
//            if (oldCap >= MAXIMUM_CAPACITY) {
//                // 设置阈值位最大整型值
//                threshold = Integer.MAX_VALUE;
//                // 并返回之前table，//TODO，返回后上层函数会不会再次校验当前table的阈值？
//                return oldTab;
//            }
//            // 否则，对容量翻倍，使用左移，效率更高
//            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
//                    oldCap >= DEFAULT_INITIAL_CAPACITY)
//                // 左移，阈值翻倍
//                newThr = oldThr << 1; // double threshold
//        }
//        // 如果之前table.length <= 0，再判断之前table的阈值是否大于0
//        // 如果之前table的阈值大于0，那么设置阈值为扩容后的容量
//        else if (oldThr > 0) // initial capacity was placed in threshold
//            newCap = oldThr;
//        else {               // zero initial threshold signifies using defaults
//            // 如果之前table.length <=0 and 之前table的阈值<=0，则用默认值设置新容量、新阈值
//            // 如使用HashMap()构造函数，之后再插入一个元素会调用resize函数，进入到这一步
//            newCap = DEFAULT_INITIAL_CAPACITY;
//            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
//        }
//        // 新阈值为0
//        if (newThr == 0) {
//            float ft = (float)newCap * loadFactor;
//            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
//                    (int)ft : Integer.MAX_VALUE);
//        }
//        threshold = newThr;
//        @SuppressWarnings({"rawtypes","unchecked"})
//        // 初始化table
//        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
//        table = newTab;
//
//        // 之前table已经初始化过
//        if (oldTab != null) {
//            // 遍历之前table的每一个Node元素
//            for (int j = 0; j < oldCap; ++j) {
//                Node<K,V> e;
//                if ((e = oldTab[j]) != null) {
//                    // 把之前table的Node元素置null，等待GC
//                    oldTab[j] = null;
//                    // 如果该结点的next指针为null，说明是唯一结点，不会是红黑树结点，则直接rehash到新table中，结束，继续遍历
//                    if (e.next == null)
//                        newTab[e.hash & (newCap - 1)] = e;
//                    // 如果该结点不是该数组索引处的唯一结点、并且是红黑树结点，把该结点加入到新table的红黑树中，结束，继续遍历
//                    else if (e instanceof TreeNode)
//                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
//                    // 如果该结点不是该数组索引处的唯一结点、并且是一个链表，则通过巧妙的rehash算法，重新排序结点，并插入到新table的链表中，结束，继续遍历
//                    else { // preserve order
//                        // 将同一个桶中的元素根据(e.hash & oldCap)是否为0进行分割，分成两个不同的链表，完成rehash
//                        // rehash的示例图请看docs下的HashMap.rehash图例.jpg
//                        // 简单说明下，如图：当容量 = 8的时候
//                        // 2 10 18 26这些hash值参与数组下标计算后，得到的下标值都是 2
//                        // 扩容后，容量 = 16，此时重新hash
//                        // 2 10 18 26计算下标的结果分别是：2、10、2、10
//                        // 也就是原来2、10、18、26散列在下标 = 2的位置，现在扩容后，分别散列在下标 = 2和 = 10的位置
//                        // 具体的rehash算法，深入研究下，怎么实现//TODO
//                        // TODO rehash算法需要研究下，rehash的算法很巧妙，具体如下：
//                        // 举例：假设容量16
//                        //  扩容前：
//                        //  n - 1 = 15      0000 0000 0000 0000 0000 0000 0000 1111
//                        //  key1(hash1) =   1111 1111 1111 1111 0000 1111 0000 0101
//                        //  key2(hash2) =   1111 1111 1111 1111 0000 1111 0001 0101
//                        //---------------------------------------------------------------------------------------------
//                        //  key1计算出来的数组索引是：0101     =   5
//                        //  key2计算出来的数组索引时：0101     =   5
//                        //  两个key计算出来的索引一致
//                        //
//                        //  扩容后：
//                        //  n - 1 = 31      0000 0000 0000 0000 0000 0000 0001 1111
//                        //  key1(hash1) =   1111 1111 1111 1111 0000 1111 0000 0101
//                        //  key2(hash2) =   1111 1111 1111 1111 0000 1111 0001 0101
//                        //---------------------------------------------------------------------------------------------
//                        //  key1计算出来的数组索引是：00101    =   5
//                        //  key2计算出来的数组索引是：10101    =   21(5 + 16)(正好是原来索引 + 之前容量)，这个是很巧妙的，归功于容量是一个二次幂数
//                        //
//                        //  因为每次扩容都是翻倍容量，而且本身又是一个二次幂数，因此在扩容后，只需看原来的hash值往左新增的那个
//                        //  bit位是1 还是 0 就好了，如果是0，索引不变，如果是1，索引就变为“原索引 + oldCap（原来的容量）”，参考
//                        //  源码理解一下，源码如下：
//
//                        // loHead存放e.hash & oldCap = 0的头节点
//                        // hiHead存放e.hash & oldCap = 1的头节点
//                        // 这两个头节点在rehash后，会被放到扩容后新table的不同索引处
//                        // loTail、hiTail节点分别作为0、1的临时尾节点，不断的
//                        Node<K,V> loHead = null, loTail = null;
//                        Node<K,V> hiHead = null, hiTail = null;
//                        Node<K,V> next;
//                        do {
//                            // 保存下一个节点
//                            next = e.next;
//                            // 原索引
//                            if ((e.hash & oldCap) == 0) {
//                                // 第一个结点，让loTail、loHead都指向它
//                                if (loTail == null)
//                                    loHead = e;
//                                else
//                                    loTail.next = e;
//                                loTail = e;
//                            }
//                            // 元索引+oldCap
//                            else {
//                                if (hiTail == null)
//                                    hiHead = e;
//                                else
//                                    hiTail.next = e;
//                                hiTail = e;
//                            }
//                        } while ((e = next) != null);
//                        // 原索引放到bucket里
//                        if (loTail != null) {
//                            loTail.next = null;
//                            newTab[j] = loHead;
//                        }
//                        // 原索引+oldCap放到bucket里
//                        if (hiTail != null) {
//                            hiTail.next = null;
//                            newTab[j + oldCap] = hiHead;
//                        }
//                    }
//                }
//            }
//        }
//        return newTab;
//    }
//
//
//    @Override
//    public Set<Entry<K, V>> entrySet() {
//        return null;
//    }
//
//
//    /*
//     * The following package-protected methods are designed to be
//     * overridden by LinkedHashMap, but not by any other subclass.
//     * Nearly all other internal methods are also package-protected
//     * but are declared final, so can be used by LinkedHashMap, view
//     * classes, and HashSet.
//     */
//
//    // Create a regular (non-tree) node
//    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
//        return new Node<>(hash, key, value, next);
//    }
//
//
//    /* ------------------------------------------------------------ */
//    // Tree bins
//
//    /**
//     * Entry for Tree bins. Extends LinkedEntry (which in turn
//     * extends Node) so can be used as extension of either regular or
//     * linked node.
//     */
//    static final class TreeNode<K,V> extends MyLinkedHashMap.Entry<K,V> {
//        TreeNode<K,V> parent;  // red-black tree links
//        TreeNode<K,V> left;
//        TreeNode<K,V> right;
//        TreeNode<K,V> prev;    // needed to unlink next upon deletion
//        boolean red;
//        TreeNode(int hash, K key, V val, Node<K,V> next) {
//            super(hash, key, val, next);
//        }
//
//        /**
//         * Returns root of tree containing this node.
//         */
//        final TreeNode<K,V> root() {
//            for (TreeNode<K,V> r = this, p;;) {
//                if ((p = r.parent) == null)
//                    return r;
//                r = p;
//            }
//        }
//
//        /**
//         * Ensures that the given root is the first node of its bin.
//         */
//        static <K,V> void moveRootToFront(Node<K,V>[] tab, TreeNode<K,V> root) {
//            int n;
//            if (root != null && tab != null && (n = tab.length) > 0) {
//                int index = (n - 1) & root.hash;
//                TreeNode<K,V> first = (TreeNode<K,V>)tab[index];
//                if (root != first) {
//                    Node<K,V> rn;
//                    tab[index] = root;
//                    TreeNode<K,V> rp = root.prev;
//                    if ((rn = root.next) != null)
//                        ((TreeNode<K,V>)rn).prev = rp;
//                    if (rp != null)
//                        rp.next = rn;
//                    if (first != null)
//                        first.prev = root;
//                    root.next = first;
//                    root.prev = null;
//                }
//                assert checkInvariants(root);
//            }
//        }
//
//        /**
//         * Finds the node starting at root p with the given hash and key.
//         * The kc argument caches comparableClassFor(key) upon first use
//         * comparing keys.
//         */
//        final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
//            TreeNode<K,V> p = this;
//            do {
//                int ph, dir; K pk;
//                TreeNode<K,V> pl = p.left, pr = p.right, q;
//                if ((ph = p.hash) > h)
//                    p = pl;
//                else if (ph < h)
//                    p = pr;
//                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
//                    return p;
//                else if (pl == null)
//                    p = pr;
//                else if (pr == null)
//                    p = pl;
//                else if ((kc != null ||
//                        (kc = comparableClassFor(k)) != null) &&
//                        (dir = compareComparables(kc, k, pk)) != 0)
//                    p = (dir < 0) ? pl : pr;
//                else if ((q = pr.find(h, k, kc)) != null)
//                    return q;
//                else
//                    p = pl;
//            } while (p != null);
//            return null;
//        }
//
//        /**
//         * Calls find for root node.
//         */
//        final TreeNode<K,V> getTreeNode(int h, Object k) {
//            return ((parent != null) ? root() : this).find(h, k, null);
//        }
//
//        /**
//         * Tie-breaking utility for ordering insertions when equal
//         * hashCodes and non-comparable. We don't require a total
//         * order, just a consistent insertion rule to maintain
//         * equivalence across rebalancings. Tie-breaking further than
//         * necessary simplifies testing a bit.
//         */
//        static int tieBreakOrder(Object a, Object b) {
//            int d;
//            if (a == null || b == null ||
//                    (d = a.getClass().getName().
//                            compareTo(b.getClass().getName())) == 0)
//                d = (System.identityHashCode(a) <= System.identityHashCode(b) ?
//                        -1 : 1);
//            return d;
//        }
//
//        /**
//         * Forms tree of the nodes linked from this node.
//         * @return root of tree
//         */
//        final void treeify(Node<K,V>[] tab) {
//            TreeNode<K,V> root = null;
//            for (TreeNode<K,V> x = this, next; x != null; x = next) {
//                next = (TreeNode<K,V>)x.next;
//                x.left = x.right = null;
//                if (root == null) {
//                    x.parent = null;
//                    x.red = false;
//                    root = x;
//                }
//                else {
//                    K k = x.key;
//                    int h = x.hash;
//                    Class<?> kc = null;
//                    for (TreeNode<K,V> p = root;;) {
//                        int dir, ph;
//                        K pk = p.key;
//                        if ((ph = p.hash) > h)
//                            dir = -1;
//                        else if (ph < h)
//                            dir = 1;
//                        else if ((kc == null &&
//                                (kc = comparableClassFor(k)) == null) ||
//                                (dir = compareComparables(kc, k, pk)) == 0)
//                            dir = tieBreakOrder(k, pk);
//
//                        TreeNode<K,V> xp = p;
//                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
//                            x.parent = xp;
//                            if (dir <= 0)
//                                xp.left = x;
//                            else
//                                xp.right = x;
//                            root = balanceInsertion(root, x);
//                            break;
//                        }
//                    }
//                }
//            }
//            moveRootToFront(tab, root);
//        }
//
//        /**
//         * Returns a list of non-TreeNodes replacing those linked from
//         * this node.
//         */
//        final Node<K,V> untreeify(MyHashMap<K,V> map) {
//            Node<K,V> hd = null, tl = null;
//            for (Node<K,V> q = this; q != null; q = q.next) {
//                Node<K,V> p = map.replacementNode(q, null);
//                if (tl == null)
//                    hd = p;
//                else
//                    tl.next = p;
//                tl = p;
//            }
//            return hd;
//        }
//
//        /**
//         * Tree version of putVal.
//         */
//        final TreeNode<K,V> putTreeVal(MyHashMap<K,V> map, Node<K,V>[] tab,
//                                               int h, K k, V v) {
//            Class<?> kc = null;
//            boolean searched = false;
//            TreeNode<K,V> root = (parent != null) ? root() : this;
//            for (TreeNode<K,V> p = root;;) {
//                int dir, ph; K pk;
//                if ((ph = p.hash) > h)
//                    dir = -1;
//                else if (ph < h)
//                    dir = 1;
//                else if ((pk = p.key) == k || (k != null && k.equals(pk)))
//                    return p;
//                else if ((kc == null &&
//                        (kc = comparableClassFor(k)) == null) ||
//                        (dir = compareComparables(kc, k, pk)) == 0) {
//                    if (!searched) {
//                        TreeNode<K,V> q, ch;
//                        searched = true;
//                        if (((ch = p.left) != null &&
//                                (q = ch.find(h, k, kc)) != null) ||
//                                ((ch = p.right) != null &&
//                                        (q = ch.find(h, k, kc)) != null))
//                            return q;
//                    }
//                    dir = tieBreakOrder(k, pk);
//                }
//
//                TreeNode<K,V> xp = p;
//                if ((p = (dir <= 0) ? p.left : p.right) == null) {
//                    Node<K,V> xpn = xp.next;
//                    TreeNode<K,V> x = map.newTreeNode(h, k, v, xpn);
//                    if (dir <= 0)
//                        xp.left = x;
//                    else
//                        xp.right = x;
//                    xp.next = x;
//                    x.parent = x.prev = xp;
//                    if (xpn != null)
//                        ((TreeNode<K,V>)xpn).prev = x;
//                    moveRootToFront(tab, balanceInsertion(root, x));
//                    return null;
//                }
//            }
//        }
//
//        /**
//         * Removes the given node, that must be present before this call.
//         * This is messier than typical red-black deletion code because we
//         * cannot swap the contents of an interior node with a leaf
//         * successor that is pinned by "next" pointers that are accessible
//         * independently during traversal. So instead we swap the tree
//         * linkages. If the current tree appears to have too few nodes,
//         * the bin is converted back to a plain bin. (The test triggers
//         * somewhere between 2 and 6 nodes, depending on tree structure).
//         */
//        final void removeTreeNode(HashMap<K,V> map, Node<K,V>[] tab,
//                                  boolean movable) {
//            int n;
//            if (tab == null || (n = tab.length) == 0)
//                return;
//            int index = (n - 1) & hash;
//            TreeNode<K,V> first = (TreeNode<K,V>)tab[index], root = first, rl;
//            TreeNode<K,V> succ = (TreeNode<K,V>)next, pred = prev;
//            if (pred == null)
//                tab[index] = first = succ;
//            else
//                pred.next = succ;
//            if (succ != null)
//                succ.prev = pred;
//            if (first == null)
//                return;
//            if (root.parent != null)
//                root = root.root();
//            if (root == null || root.right == null ||
//                    (rl = root.left) == null || rl.left == null) {
//                tab[index] = first.untreeify(map);  // too small
//                return;
//            }
//            TreeNode<K,V> p = this, pl = left, pr = right, replacement;
//            if (pl != null && pr != null) {
//                TreeNode<K,V> s = pr, sl;
//                while ((sl = s.left) != null) // find successor
//                    s = sl;
//                boolean c = s.red; s.red = p.red; p.red = c; // swap colors
//                TreeNode<K,V> sr = s.right;
//                TreeNode<K,V> pp = p.parent;
//                if (s == pr) { // p was s's direct parent
//                    p.parent = s;
//                    s.right = p;
//                }
//                else {
//                    TreeNode<K,V> sp = s.parent;
//                    if ((p.parent = sp) != null) {
//                        if (s == sp.left)
//                            sp.left = p;
//                        else
//                            sp.right = p;
//                    }
//                    if ((s.right = pr) != null)
//                        pr.parent = s;
//                }
//                p.left = null;
//                if ((p.right = sr) != null)
//                    sr.parent = p;
//                if ((s.left = pl) != null)
//                    pl.parent = s;
//                if ((s.parent = pp) == null)
//                    root = s;
//                else if (p == pp.left)
//                    pp.left = s;
//                else
//                    pp.right = s;
//                if (sr != null)
//                    replacement = sr;
//                else
//                    replacement = p;
//            }
//            else if (pl != null)
//                replacement = pl;
//            else if (pr != null)
//                replacement = pr;
//            else
//                replacement = p;
//            if (replacement != p) {
//                TreeNode<K,V> pp = replacement.parent = p.parent;
//                if (pp == null)
//                    root = replacement;
//                else if (p == pp.left)
//                    pp.left = replacement;
//                else
//                    pp.right = replacement;
//                p.left = p.right = p.parent = null;
//            }
//
//            TreeNode<K,V> r = p.red ? root : balanceDeletion(root, replacement);
//
//            if (replacement == p) {  // detach
//                TreeNode<K,V> pp = p.parent;
//                p.parent = null;
//                if (pp != null) {
//                    if (p == pp.left)
//                        pp.left = null;
//                    else if (p == pp.right)
//                        pp.right = null;
//                }
//            }
//            if (movable)
//                moveRootToFront(tab, r);
//        }
//
//        /**
//         * Splits nodes in a tree bin into lower and upper tree bins,
//         * or untreeifies if now too small. Called only from resize;
//         * see above discussion about split bits and indices.
//         *
//         * @param map the map
//         * @param tab the table for recording bin heads
//         * @param index the index of the table being split
//         * @param bit the bit of hash to split on
//         */
//        final void split(MyHashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
//            TreeNode<K,V> b = this;
//            // Relink into lo and hi lists, preserving order
//            TreeNode<K,V> loHead = null, loTail = null;
//            TreeNode<K,V> hiHead = null, hiTail = null;
//            int lc = 0, hc = 0;
//            for (TreeNode<K,V> e = b, next; e != null; e = next) {
//                next = (TreeNode<K,V>)e.next;
//                e.next = null;
//                if ((e.hash & bit) == 0) {
//                    if ((e.prev = loTail) == null)
//                        loHead = e;
//                    else
//                        loTail.next = e;
//                    loTail = e;
//                    ++lc;
//                }
//                else {
//                    if ((e.prev = hiTail) == null)
//                        hiHead = e;
//                    else
//                        hiTail.next = e;
//                    hiTail = e;
//                    ++hc;
//                }
//            }
//
//            if (loHead != null) {
//                if (lc <= UNTREEIFY_THRESHOLD)
//                    tab[index] = loHead.untreeify(map);
//                else {
//                    tab[index] = loHead;
//                    if (hiHead != null) // (else is already treeified)
//                        loHead.treeify(tab);
//                }
//            }
//            if (hiHead != null) {
//                if (hc <= UNTREEIFY_THRESHOLD)
//                    tab[index + bit] = hiHead.untreeify(map);
//                else {
//                    tab[index + bit] = hiHead;
//                    if (loHead != null)
//                        hiHead.treeify(tab);
//                }
//            }
//        }
//
//        /* ------------------------------------------------------------ */
//        // Red-black tree methods, all adapted from CLR
//
//        static <K,V> TreeNode<K,V> rotateLeft(TreeNode<K,V> root,
//                                                      TreeNode<K,V> p) {
//            TreeNode<K,V> r, pp, rl;
//            if (p != null && (r = p.right) != null) {
//                if ((rl = p.right = r.left) != null)
//                    rl.parent = p;
//                if ((pp = r.parent = p.parent) == null)
//                    (root = r).red = false;
//                else if (pp.left == p)
//                    pp.left = r;
//                else
//                    pp.right = r;
//                r.left = p;
//                p.parent = r;
//            }
//            return root;
//        }
//
//        static <K,V> TreeNode<K,V> rotateRight(TreeNode<K,V> root,
//                                                       TreeNode<K,V> p) {
//            TreeNode<K,V> l, pp, lr;
//            if (p != null && (l = p.left) != null) {
//                if ((lr = p.left = l.right) != null)
//                    lr.parent = p;
//                if ((pp = l.parent = p.parent) == null)
//                    (root = l).red = false;
//                else if (pp.right == p)
//                    pp.right = l;
//                else
//                    pp.left = l;
//                l.right = p;
//                p.parent = l;
//            }
//            return root;
//        }
//
//        static <K,V> TreeNode<K,V> balanceInsertion(TreeNode<K,V> root,
//                                                            TreeNode<K,V> x) {
//            x.red = true;
//            for (TreeNode<K,V> xp, xpp, xppl, xppr;;) {
//                if ((xp = x.parent) == null) {
//                    x.red = false;
//                    return x;
//                }
//                else if (!xp.red || (xpp = xp.parent) == null)
//                    return root;
//                if (xp == (xppl = xpp.left)) {
//                    if ((xppr = xpp.right) != null && xppr.red) {
//                        xppr.red = false;
//                        xp.red = false;
//                        xpp.red = true;
//                        x = xpp;
//                    }
//                    else {
//                        if (x == xp.right) {
//                            root = rotateLeft(root, x = xp);
//                            xpp = (xp = x.parent) == null ? null : xp.parent;
//                        }
//                        if (xp != null) {
//                            xp.red = false;
//                            if (xpp != null) {
//                                xpp.red = true;
//                                root = rotateRight(root, xpp);
//                            }
//                        }
//                    }
//                }
//                else {
//                    if (xppl != null && xppl.red) {
//                        xppl.red = false;
//                        xp.red = false;
//                        xpp.red = true;
//                        x = xpp;
//                    }
//                    else {
//                        if (x == xp.left) {
//                            root = rotateRight(root, x = xp);
//                            xpp = (xp = x.parent) == null ? null : xp.parent;
//                        }
//                        if (xp != null) {
//                            xp.red = false;
//                            if (xpp != null) {
//                                xpp.red = true;
//                                root = rotateLeft(root, xpp);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        static <K,V> TreeNode<K,V> balanceDeletion(TreeNode<K,V> root,
//                                                           TreeNode<K,V> x) {
//            for (TreeNode<K,V> xp, xpl, xpr;;)  {
//                if (x == null || x == root)
//                    return root;
//                else if ((xp = x.parent) == null) {
//                    x.red = false;
//                    return x;
//                }
//                else if (x.red) {
//                    x.red = false;
//                    return root;
//                }
//                else if ((xpl = xp.left) == x) {
//                    if ((xpr = xp.right) != null && xpr.red) {
//                        xpr.red = false;
//                        xp.red = true;
//                        root = rotateLeft(root, xp);
//                        xpr = (xp = x.parent) == null ? null : xp.right;
//                    }
//                    if (xpr == null)
//                        x = xp;
//                    else {
//                        TreeNode<K,V> sl = xpr.left, sr = xpr.right;
//                        if ((sr == null || !sr.red) &&
//                                (sl == null || !sl.red)) {
//                            xpr.red = true;
//                            x = xp;
//                        }
//                        else {
//                            if (sr == null || !sr.red) {
//                                if (sl != null)
//                                    sl.red = false;
//                                xpr.red = true;
//                                root = rotateRight(root, xpr);
//                                xpr = (xp = x.parent) == null ?
//                                        null : xp.right;
//                            }
//                            if (xpr != null) {
//                                xpr.red = (xp == null) ? false : xp.red;
//                                if ((sr = xpr.right) != null)
//                                    sr.red = false;
//                            }
//                            if (xp != null) {
//                                xp.red = false;
//                                root = rotateLeft(root, xp);
//                            }
//                            x = root;
//                        }
//                    }
//                }
//                else { // symmetric
//                    if (xpl != null && xpl.red) {
//                        xpl.red = false;
//                        xp.red = true;
//                        root = rotateRight(root, xp);
//                        xpl = (xp = x.parent) == null ? null : xp.left;
//                    }
//                    if (xpl == null)
//                        x = xp;
//                    else {
//                        TreeNode<K,V> sl = xpl.left, sr = xpl.right;
//                        if ((sl == null || !sl.red) &&
//                                (sr == null || !sr.red)) {
//                            xpl.red = true;
//                            x = xp;
//                        }
//                        else {
//                            if (sl == null || !sl.red) {
//                                if (sr != null)
//                                    sr.red = false;
//                                xpl.red = true;
//                                root = rotateLeft(root, xpl);
//                                xpl = (xp = x.parent) == null ?
//                                        null : xp.left;
//                            }
//                            if (xpl != null) {
//                                xpl.red = (xp == null) ? false : xp.red;
//                                if ((sl = xpl.left) != null)
//                                    sl.red = false;
//                            }
//                            if (xp != null) {
//                                xp.red = false;
//                                root = rotateRight(root, xp);
//                            }
//                            x = root;
//                        }
//                    }
//                }
//            }
//        }
//
//        /**
//         * Recursive invariant check
//         */
//        static <K,V> boolean checkInvariants(TreeNode<K,V> t) {
//            TreeNode<K,V> tp = t.parent, tl = t.left, tr = t.right,
//                    tb = t.prev, tn = (TreeNode<K,V>)t.next;
//            if (tb != null && tb.next != t)
//                return false;
//            if (tn != null && tn.prev != t)
//                return false;
//            if (tp != null && t != tp.left && t != tp.right)
//                return false;
//            if (tl != null && (tl.parent != t || tl.hash > t.hash))
//                return false;
//            if (tr != null && (tr.parent != t || tr.hash < t.hash))
//                return false;
//            if (t.red && tl != null && tl.red && tr != null && tr.red)
//                return false;
//            if (tl != null && !checkInvariants(tl))
//                return false;
//            if (tr != null && !checkInvariants(tr))
//                return false;
//            return true;
//        }
//    }
//
//}
