package org.tic.vertx.examples.eb.socket;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketClientVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(SocketClientVerticle.class);

    @Override
    public void start() throws Exception {
        NetClient client = vertx.createNetClient();

        client.connect(9999, "localhost", event -> {
            if (event.succeeded()) {
                logger.debug("connected ok!");

                //下面是无法发送消息到远程socket服务器上的
                //sendBus("hello", "hello, my name is vert.x");
                NetSocket socket = event.result();

                //这里每隔2秒，向远程服务器发送消息
                //注意，message一定要是Buffer类型，不能是String
                vertx.setPeriodic(2000, h -> {
                    //正确做法
                    sendBus(socket.writeHandlerID(), Buffer.buffer("hello, my name is vert.x"));
                    //错误做法
//                    sendBus(socket.writeHandlerID(), Buffer.buffer("hello, my name is vert.x").toString(Charset.defaultCharset()));
                });

                //通过socket直接写数据给远程服务器
                //socket.write(Buffer.buffer("hello, my name is vert.x").toString(Charset.defaultCharset()));
            } else {
                logger.error("connected failed!");
            }
        });
    }

    private void sendBus(String address, Object data) {
        this.vertx.eventBus().send(address, data);
    }

}
