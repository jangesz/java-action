package org.tic.guice.helloworld;

import com.google.inject.AbstractModule;
import org.tic.guice.helloworld.helloworlddemo.HelloWorldModule;

// APP 总的module，通过install的方法来嵌套配置module
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new HelloWorldModule());
    }

}
