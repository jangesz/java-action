package org.tic.netty.example.server;

import org.tic.netty.tic.server.FrontServerBuilder;
import org.tic.netty.tic.server.api.Listener;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/26.
 */
public class FrontServerTest {

    public static void main(String[] args) {
        new FrontServerBuilder().build().start();
    }

}
