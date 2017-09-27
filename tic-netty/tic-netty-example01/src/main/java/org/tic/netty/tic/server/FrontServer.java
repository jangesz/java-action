package org.tic.netty.tic.server;

import io.netty.channel.ChannelHandler;
import org.tic.netty.tic.server.handler.ServerChannelHandler;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/26.
 */
public class FrontServer extends NettyTCPServer0 {

    protected String host = "127.0.0.1";
    protected int port = 9420;
    private ChannelHandler channelHandler;

    public FrontServer() {
        //初始化服务端的ChannelHandler，处理客户端连接、断开连接、异常、消息读取等
        this.channelHandler = new ServerChannelHandler();
    }

//    public FrontServer(String host, int port) {
//        //初始化服务端绑定的地址和IP端口
//        super(host, port);
//        //初始化服务端启动状态
////        super.init();
        //初始化连接管理器
//        this.connectionManager = new ServerConnectionManager(false);
        //初始化服务端接收到的消息分发器
//        this.messageDispatcher = new MessageDispatcher();
        //初始化服务端的ChannelHandler，处理客户端连接、断开连接、异常、消息读取等
//        this.channelHandler = new ServerChannelHandler(true, connectionManager, messageDispatcher);
//    }

    @Override
    protected void init() {
        super.address(this.host, this.port);

        super.init();
    }

    @Override
    ChannelHandler getDecoder() {
        return null;
    }

    @Override
    ChannelHandler getEncoder() {
        return null;
    }

    @Override
    ChannelHandler getChannelHandler() {
        return this.channelHandler;
    }

    public void start() {
//        if (this.jPushServer == null) {
//            throw new RuntimeException("必须配置JPushServer实例");
//        }
//        initMessageDispatcher();
        super.start();
    }
}
