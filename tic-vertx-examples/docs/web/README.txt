tic-vertx-examples-web-router
    WebRouterExample01Verticle：一个基础的使用Router配置HTTP请求路径的例子



queryParams()
    route定义：/api/user
    例子：/api/user?a=1&b=2
    queryParams() => a:1, b:2
    queryParam("a") => 1
pathParams()
    route定义：/api/user/:id
    例子：/api/user/123
    pathParams() => id:123
    pathParam("id") => 123



method()    指定请求的方法类型：GET、POST、PUT、DELETE、PATCH 等
path()      指定路径，可以有占位符(:id)，通过pathParam获取路径参数
routeWithRegex()    正则匹配路由
consumes()  指定请求媒体类型（MIME types）来匹配路由
    text/html
    application/json
    text/xml
    text/plain
    text/*
    等等

produces()  指定返回的数据媒体类型（MIME types）


通过以上几种来确定一个请求，如果都匹配不到，会返回默认的Resource Not Found

