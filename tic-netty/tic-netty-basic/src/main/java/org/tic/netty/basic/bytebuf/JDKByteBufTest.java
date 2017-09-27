package org.tic.netty.basic.bytebuf;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;
import org.junit.Test;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/27.
 */
public class JDKByteBufTest {

    @Test
    public void test01() {
//        PooledDirectByteBuf
//        UnpooledDirectByteBuf
        UnpooledByteBufAllocator alloc1 = UnpooledByteBufAllocator.DEFAULT;
        PooledByteBufAllocator alloc2 = PooledByteBufAllocator.DEFAULT;

//        UnpooledDirectByteBuf
    }

}
