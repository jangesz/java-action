参考文献：
http://www.codeweblog.com/%E6%9C%8D%E5%8A%A1%E5%8F%91%E7%8E%B0%E7%B3%BB%E7%BB%9Fconsul%E4%BB%8B%E7%BB%8D/
http://www.techweb.com.cn/network/system/2016-01-28/2270190.shtml
https://studygolang.com/articles/4837
https://www.cnblogs.com/xishuai/p/macos-ubuntu-install-consul.html
https://www.cnblogs.com/shanyou/p/6286207.html

1. 下载/安装
consul 1.0.0
47.97.19.109 外
10.81.45.2 内

./consul agent -server -bind=10.81.45.2 -bootstrap-expect 1 -data-dir /tmp/consul/ -config-dir=/etc/consul.d/
./consul agent -server -bind=47.97.19.109 -bootstrap-expect 1 -data-dir /tmp/consul/ -config-dir=/etc/consul.d/

后台运行，加上 &
./consul agent -server -bind=47.97.19.109 -bootstrap-expect 1 -data-dir /tmp/consul/ -config-dir=/etc/consul.d/ -ui -client=0.0.0.0 &

允许远程客户端连接
-client=0.0.0.0

2. 运行
consul agent -dev -bind=47.97.19.109

3. 服务注册
    方法一、服务定义：是服务注册最重用的方法，步骤如下：

    1. 创建配置文件目录：
    mkdir /etc/consul.d/

    consul.d是配置文件目录，表示里面有若干个配置文件，这是命名规范
    > echo '{"service": {"name": "web", "tags": ["rails"], "port": 80}}' > /etc/consul.d/web.json
    这段配置内容的意思是：有个名称为web的服务运行在端口80，另外给他一个标签作为额外的方法查询服务

    2. 启动代理并加载配置文件
    consul agent -dev -bind=xxx.xxx.xxx.xxx -config-dir=/etc/consul.d/


    方法二、HTTP API：通过HTTP API方式注册，如下：
    curl -X PUT http://127.0.0.1:8500/v1/agent/service/register -i -H "Content-Type:application/json" -H "Accept:application/json" -d '{"ID":"web","Name" :"etcd","Tags":["2.2.2","cn-north-1","develop"],"Address":"10.10.10.1","Port":8080}'

