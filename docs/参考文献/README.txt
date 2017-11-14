1. MODULAR ROUTER DESIGN FOR VERT.X MICROSERVICES
https://www.devcon5.ch/en/blog/2017/09/15/vertx-modular-router-design/
    由于部署Verticle（在相同端口提供不同的路由）不会让每个端点有效。这是因为路由不是共享的，当一个HTTP请求到达某个路由，
    只有第一个路由才能响应。怎么解决这个问题呢？

    两种方式来解决：
        1. 单个Verticle，通过子路由
