package org.tic.vertx.examples.eb.socket;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketServerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(SocketServerVerticle.class);

    @Override
    public void start() throws Exception {
        NetServer server = vertx.createNetServer();

        server.connectHandler(socket -> {
            //当有连接进入的时候
            logger.debug(socket.remoteAddress() + " connected!");

            //当有客户端发送数据过来的时候
            socket.handler(buffer -> {
                logger.debug("receive data from remote " + socket.remoteAddress() + " , message data = " + buffer.toString());
            });

            //1. 远程主机强迫关闭了一个现有的连接。是因为远程客户端强行关闭了连接
            socket.closeHandler(close -> {
                logger.warn("remote instance has been shutdown, lost connection!");
            });

            socket.exceptionHandler(t -> {
                System.out.println("occur an exception -> " + t.getMessage());
            });
        });

        server.listen(9999, "localhost", res -> {
            if (res.succeeded()) {
                logger.debug("Net server started in port " + 9999);
            } else {
                logger.error("Net server started failed, bind error");
            }
        });
    }
}
