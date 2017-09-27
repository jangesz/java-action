package org.tic.netty.tic.server;

import org.tic.netty.tic.server.api.Listener;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/26.
 */
public class FrontServerBuilder {

    private FrontServer frontServer;

    public FrontServerBuilder() {
        this.frontServer = new FrontServer();
    }

    public FrontServer startListener(Listener listener) {
        this.frontServer.startListener = listener;
        return this.frontServer;
    }

    public FrontServer stopListener(Listener listener) {
        this.frontServer.stopListener = listener;
        return this.frontServer;
    }

    public FrontServer host(String host) {
        this.frontServer.host = host;
        return this.frontServer;
    }

    public FrontServer port(int port) {
        this.frontServer.port = port;
        return this.frontServer;
    }

    public FrontServer build() {
        return this.frontServer;
    }
}
