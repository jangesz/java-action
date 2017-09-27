package org.tic.netty.basic.leak;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/27.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private Runnable loadRunner;

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        ctx.channel().config().setWriteBufferHighWaterMark(10 * 1024 * 1024);

        loadRunner = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ByteBuf msg = null;
                while(true) {
                    if (ctx.channel().isWritable()) {
                        msg = Unpooled.wrappedBuffer("Netty OOM Example".getBytes());
                        ctx.writeAndFlush(msg);
                    } else {
                        System.out.println("the write queue is busy......");
                    }
                }
            }
        };
        new Thread(loadRunner, "LoadRunner-Thread").start();
//        ctx.writeAndFlush(f)
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
