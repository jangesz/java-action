package org.tic.netty.basic.bytebuf;

import io.netty.buffer.AbstractReferenceCountedByteBuf;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import sun.nio.ch.DirectBuffer;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/27.
 */
public class AbstractReferenceCountedByteBufTest {

    @Test
    public void test01() {
//        AbstractReferenceCountedByteBuf
        ChannelHandlerContext ctx = null;
        ByteBuf buf = ctx.alloc().directBuffer();


    }

}
