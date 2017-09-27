package org.tic.netty.basic.bytebuf;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ByteProcessor;

/**
 * 描述信息：
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/27.
 */
public class ByteBufReferenceCountedTest {

    public void test01() {
        ByteBuf buf;
        AbstractByteBuf byteBuf;
    }

}
/*
    抽象ByteBuf 实现 ReferenceCounted，而ReferenceCounted是一个对象引用计数器接口，主要记录对象的引用数量，当引用数量为0时，表示可以回收对象，在调试模式下，
    如果发现对象出现内存泄漏，可以用touch方法记录操作的相关信息，通过ResourceLeakDetector获取操作的相关信息，以便分析内存泄漏的原因。

    ByteBuf（抽象字节Buf）
        capacity：最大容量限制，如果用户尝试用#capacity(int)和#ensureWritable(int)方法增加buf容量，当增加的容量超过最大容量，就会抛出非法参数异常。


    ByteBuf有两个索引，readerIndex（读索引）和writerIndex（写索引），需要注意的是：
        1. readerIndex不能大于writerIndex
        2. writerIndex不能小于readerIndex
        3. buf可读字节数 = writerIndex - readerIndex
        4. buf可写字节数 = capacity - writerIndex
        5. buf最大可写字节数 = maxCapacity - writerIndex

        a. 可以使用markReader/WriterIndex标记当前buf读写索引位置
        b. 可以使用resetReader/WriterIndex方法重回先前标记的索引位置
        c. 当内存空间负载过度时，可以使用discardReadBytes丢弃一些数据，以节省空间
        d. 可以用ensureWritable检测buf是否有足够的空间写数据
        e. 提供getBytes方法，可以将buf中的数据转移到目的ByteBuf，Byte数组。

*/



























