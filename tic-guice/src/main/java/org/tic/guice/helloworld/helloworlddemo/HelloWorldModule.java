package org.tic.guice.helloworld.helloworlddemo;

import com.google.inject.AbstractModule;

import java.io.PrintStream;

// 把configuration类要干的事情，在这里做掉
public class HelloWorldModule extends AbstractModule {
    @Override
    protected void configure() {
        // 查看APP类
        // 1. 首先一个MyApplet的具体实现类，通过Guice的bind来实现绑定
        // 没有Inject，to(StringWritingApplect.class)会有黄色的提示信息：intellij idea 提示 Class xxxx is uninstantiable
        bind(MyApplet.class).to(StringWritingApplet.class);
        bind(MyDestination.class).to(PrintStreamWriter.class);
        bind(PrintStream.class).toInstance(System.out);

        // 通过Output注解的方式，只给有这个注解的地方提供Hello World的输出
        bind(String.class).annotatedWith(Output.class).toInstance("Hello World");
    }
}
