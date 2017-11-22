package org.tic.guice.helloworld;

import com.google.inject.Guice;
import org.tic.guice.helloworld.helloworlddemo.MyApplet;

public class App {
    /**
     * bootstrap
     * parse command line
     * set up environment
     * kick off main logic
     * @param args parameters
     */
    public static void main(String[] args) {
//        MyApplet mainApplet = Configuration.getMainApplet();
//        mainApplet.run();

        MyApplet mainApplet = Guice.createInjector(new AppModule()).getInstance(MyApplet.class);
        mainApplet.run();
    }
}
