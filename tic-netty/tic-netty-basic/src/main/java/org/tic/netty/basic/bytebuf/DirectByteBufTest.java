package org.tic.netty.basic.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import org.junit.Test;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/27.
 */
public class DirectByteBufTest {

    /**
     * 测试创建直接内存分配的ByteBuf，并且释放引用计数对象，最后尝试在引用计数为0的引用计数对象上写数据，抛出异常
     */
    @Test
    public void test01() {
        PooledByteBufAllocator alloc = new PooledByteBufAllocator();
        ByteBuf buf = alloc.directBuffer();
        System.out.println(buf.refCnt());

        buf.release();
        System.out.println(buf.refCnt());
        assert buf.refCnt() == 0;

        //尝试访问引用计数为0的引用计数对象会抛出异常信息
        buf.writeByte(1);
    }

    @Test
    public void test02() {
        PooledByteBufAllocator alloc = new PooledByteBufAllocator();
        ByteBuf buf = alloc.directBuffer();
        System.out.println(buf.refCnt());
        buf.release();
        System.out.println(buf.refCnt());
        //retain抛出异常，因为引用计数对象的引用计数 = 0了
        buf.retain();
        System.out.println(buf.refCnt());
    }

    @Test
    public void test03() {
        PooledByteBufAllocator alloc = new PooledByteBufAllocator();
        ByteBuf buf = alloc.directBuffer();
        /*
         *
         */
        c(b(a(buf)));
        System.out.println(buf.refCnt());
    }

    //分配200个不释放，看看会不会有内存泄漏
    @Test
    public void test04() throws InterruptedException {
        PooledByteBufAllocator alloc = new PooledByteBufAllocator();
        for (int i = 0; i<300; i++) {
            ByteBuf buf = alloc.directBuffer();
            buf.writeByte(42);
        }
    }

    private ByteBuf a(ByteBuf input) {
        input.writeByte(42);
        return input;
    }

    private ByteBuf b(ByteBuf input) {
        try {
            ByteBuf output = input.alloc().directBuffer(input.readableBytes() + 1);
            output.writeBytes(input);
            output.writeByte(42);
            return output;
        } finally {
            input.release();
        }
    }

    private void c(ByteBuf input) {
        System.out.println(input);
        input.release();
    }

}

/*
                    ByteBufAllocator
                        |
                AbstractByteBufAllocator
                |               |
UnpooledByteBufAllocator        PooledByteBufAllocator

ByteBufAllocator主要用来生成三种ByteBuf：HeapBuffer、DirectBuffer、CompositeBuffer



*/


























