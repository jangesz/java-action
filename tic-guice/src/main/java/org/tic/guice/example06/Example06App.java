package org.tic.guice.example06;

import java.io.PrintStream;

public class Example06App {

    public static void main(String[] args) {
        printHelloWorld().println("HelloWorld");
    }

    private static PrintStream printHelloWorld() {
        return System.out;
    }

}
