package org.tic.java8.generic.clazz;

/**
 * 常用的泛型代表的意思：
 *  E（element）
 *  K（key）
 *  V（value）
 *
 *
 *  类型变量的限定
 *      <T extends BoundingType>
 *      <T extends BoundingType & SecondType>
 *
 */
public class GenericClass<T, U> {
    private T first;
    private U second;

    public GenericClass(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }
}
