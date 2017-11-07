package org.tic.java8.stream;
/*
Stream方法
    distinct 去重（依赖元素的equals方法）
    filter 过滤（用给定的过滤函数对元素做过滤操作）
    map 转换（用给定的转换函数对元素进行转换）
        mapToInt
        mapToLong
        mapToDouble
        三个对于原始类型的变种方法，目的是免除自动装箱/拆箱的额外损耗

    flatMap 转换（和map类似，不同的是其每个元素转换得到的是Stream对象，会把子Stream中的元素压缩到父集合中）

    peek 生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例），新Stream每个元素被消费的时候都会执行给定的消费函数

    limit 截断

    skip 取剩下









*/