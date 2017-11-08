package org.tic.vertx.examples.eb.cluster;

/*
* 集群模式部署Vert.x（通过hazelcast），引入jar
    <dependency>
        <groupId>io.vertx</groupId>
        <artifactId>vertx-hazelcast</artifactId>
    </dependency>
* 1. 运行ClusterExample01Launcher
* 2. 运行ClusterExample01AnotherLauncher
* 3. 可以看到不同Vert.x实例间能够通过EventBus来发送消息
*
*/