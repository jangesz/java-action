package org.tic.netty.tic.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tic.netty.tic.server.api.AbstractNettyServer;
import org.tic.netty.tic.server.api.Listener;
import org.tic.netty.tic.server.api.NettyServer;
import org.tic.netty.tic.server.exception.ServerException;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/26.
 */
public abstract class NettyTCPServer0 extends AbstractNettyServer implements NettyServer {
    private final Logger logger = LoggerFactory.getLogger(NettyTCPServer0.class);

    public enum State {Created, Initialized, Starting, Started, Shutdown}

    private final AtomicReference<State> serverState = new AtomicReference<>(NettyTCPServer0.State.Created);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private String host;
    private int port;

    protected Listener startListener;
    protected Listener stopListener;

    public void start() {
        if (!serverState.compareAndSet(NettyTCPServer0.State.Initialized, NettyTCPServer0.State.Starting)) {
            throw new ServerException("Server already started or have not init");
        }

        createEpollServer();
    }

    protected void init() {
        if (this.host == null || this.host.equals("")
                || this.port == 0) throw new ServerException("Server host and port are not set yet!");

        if (!serverState.compareAndSet(NettyTCPServer0.State.Created, NettyTCPServer0.State.Initialized)) {
            throw new ServerException("Server already init");
        }
    }

    protected void address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void createEpollServer() {
        EventLoopGroup bossGroup = getBossGroup();
        EventLoopGroup workerGroup = getWorkerGroup();

        if (bossGroup == null) {
            EpollEventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup(getBossThreadNum(), getBossThreadFactory());
            epollEventLoopGroup.setIoRatio(100);
            bossGroup = epollEventLoopGroup;
        }

        if (workerGroup == null) {
            EpollEventLoopGroup epollEventLoopGroup = new EpollEventLoopGroup(getWorkerThreadNum(), getWorkerThreadFactory());
            epollEventLoopGroup.setIoRatio(getIoRate());
            workerGroup = epollEventLoopGroup;
        }

        createServer(bossGroup, workerGroup, EpollServerSocketChannel::new);
    }

    private void createServer(EventLoopGroup boss, EventLoopGroup work, ChannelFactory<? extends ServerChannel> channelFactory) {
        /*
         * NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，
         * Netty提供了许多不同的EventLoopGroup的实现用来处理不同传输协议。
         * 在一个服务端的应用会有2个NioEventLoopGroup会被使用。
         * 第一个经常被叫做‘boss’，用来接收进来的连接。
         * 第二个经常被叫做‘worker’，用来处理已经被接收的连接，
         * 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
         * 如何知道多少个线程已经被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，
         * 并且可以通过构造函数来配置他们的关系。
         */
        this.bossGroup = boss;
        this.workerGroup = work;

        try {
            /*
             * ServerBootstrap 是一个启动NIO服务的辅助启动类
             * 你可以在这个服务中直接使用Channel
             */
            ServerBootstrap b = new ServerBootstrap();

            /*
             * 这一步是必须的，如果没有设置group将会报java.lang.IllegalStateException: group not set异常
             */
            b.group(bossGroup, workerGroup);

            /*
             * ServerSocketChannel以NIO的selector为基础进行实现的，用来接收新的连接
             * 这里告诉Channel如何获取新的连接.
             */
            b.channelFactory(channelFactory);


            /*
             * 这里的事件处理类经常会被用来处理一个最近的已经接收的Channel。
             * ChannelInitializer是一个特殊的处理类，
             * 他的目的是帮助使用者配置一个新的Channel。
             * 也许你想通过增加一些处理类比如NettyServerHandler来配置一个新的Channel
             * 或者其对应的ChannelPipeline来实现你的网络程序。
             * 当你的程序变的复杂时，可能你会增加更多的处理类到pipeline上，
             * 然后提取这些匿名类到最顶层的类上。
             */
            b.childHandler(new ChannelInitializer<Channel>() { // (4)
                @Override
                public void initChannel(Channel ch) throws Exception {//每连上一个链接调用一次
                    initPipeline(ch.pipeline());
                }
            });

            initOptions(b);

            /*
             * 绑定端口并启动去接收进来的连接
             */
            InetSocketAddress address = (null == host || host.equals("")) ? new InetSocketAddress(port) : new InetSocketAddress(host, port);
            b.bind(address).addListener(future -> {
                if (future.isSuccess()) {
                    serverState.set(NettyTCPServer0.State.Started);
                    logger.info("server start success on:{}", port);
                    if (this.startListener != null) this.startListener.onSuccess(port);
                } else {
                    logger.error("server start failure on:{}", port, future.cause());
                    if (this.startListener != null) this.startListener.onFailure(future.cause());
                }
            });
        } catch (Exception e) {
            logger.error("server start exception", e);
            if (this.startListener != null) this.startListener.onFailure(e);
            throw new ServerException("server start exception, port=" + port, e);
        }
    }

    /**
     * 每连上一个链接调用一次
     */
    private void initPipeline(ChannelPipeline pipeline) {
        pipeline.addLast("decoder", getDecoder());
        pipeline.addLast("encoder", getEncoder());
        pipeline.addLast("handler", getChannelHandler());
    }

    /***
     * option()是提供给NioServerSocketChannel用来接收进来的连接。
     * childOption()是提供给由父管道ServerChannel接收到的连接，
     * 在这个例子中也是NioServerSocketChannel。
     */
    private void initOptions(ServerBootstrap b) {
        //b.childOption(ChannelOption.SO_KEEPALIVE, false);// 使用应用层心跳
        /*
         * 在Netty 4中实现了一个新的ByteBuf内存池，它是一个纯Java版本的 jemalloc （Facebook也在用）。
         * 现在，Netty不会再因为用零填充缓冲区而浪费内存带宽了。不过，由于它不依赖于GC，开发人员需要小心内存泄漏。
         * 如果忘记在处理程序中释放缓冲区，那么内存使用率会无限地增长。
         * Netty默认不使用内存池，需要在创建客户端或者服务端的时候进行指定
         */
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }


    abstract ChannelHandler getDecoder();
    abstract ChannelHandler getEncoder();
    abstract ChannelHandler getChannelHandler();
}
