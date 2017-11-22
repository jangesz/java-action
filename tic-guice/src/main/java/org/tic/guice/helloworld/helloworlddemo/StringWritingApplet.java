package org.tic.guice.helloworld.helloworlddemo;

import com.google.inject.Provider;

import javax.inject.Inject;

public class StringWritingApplet implements MyApplet {

    private MyDestination destination;

    // 这里改造outputString，改成StringProvider，这样灵活性大
    // 有些时候获取outputString的方式并不是直接提供的，可能是通过block在某个io上，等待获取后才取得
    // 这个时候直接通过这种outputString的方式传入就显得不灵活
    private Provider<String> stringProvider;

    @Inject
    public StringWritingApplet(MyDestination destination, @Output Provider<String> stringProvider) {
        this.destination = destination;
        this.stringProvider = stringProvider;
    }

    private void writeString() {
        // 这里的赋值语句destination = System.out;不是我们需要的，这个其实就是dependency注入的过程
        // 通过这个例子，我们是否可以在以后的编程过程中，看到赋值实现类的地方，都有心的思考下是否
        // 是一个dependency注入的过程，是否可以剥离，抽象，最终来消除dependency？？

        // 这里我们通过建立一个构造函数，来强制实例化这个类的时候，注入我们需要的dependency。
        // Alt + Insert
//        destination = System.out;

        // 最后，终于把System.out.println("Hello World")这样的hardcode，换成了如下的抽象代码
        // destination.write(outputString)，意思是向某个地方通过某个方法输出某种字符串
        destination.write(stringProvider.get());
    }

    public void run() {
        writeString();
    }
}
