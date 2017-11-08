package org.tic.vertx.examples.basic.example03;

/*
*
* 1. 运行BasicExample03Launcher
* 2. 运行BasicExample03AnotherLauncher
* 3. Basic09Verticle通过EventBus，publish消息
* 4. Basic07Verticle、Basic08Verticle能够消费09 publish的EventBus消息，而Basic10Verticle无法消费到09 publish的EventBus消息
*
* 5. 结论：一个Vert.x应用可以包含多个Verticle实例，实例之间可以通过EventBus通讯。
* 而不同的Vert.x应用中的Verticle实例不能通过EventBus通讯。
*
*/