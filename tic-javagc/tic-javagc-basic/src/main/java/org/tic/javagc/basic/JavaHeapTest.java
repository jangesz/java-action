package org.tic.javagc.basic;

/**
 * 描述信息：//TODO
 * Jange Gu
 * http://jange.me
 * http://1gb.tech
 * 2017/9/27.
 */
public class JavaHeapTest {
    public final static int OUTOFMEMORY = 200000000;
    private String oom;
    private int length;
    StringBuffer tempOOM = new StringBuffer();

    public JavaHeapTest(int length){
        this.length = length;

        int i = 0;
        while (i<length) {
            i++;
            try {
                tempOOM.append("a");
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                break;
            }
        }
        this.oom = tempOOM.toString();
    }

    public String getOom() {
        return oom;
    }

    public int getLength() {
        return length;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(5000);
        JavaHeapTest javaHeapTest = new JavaHeapTest(OUTOFMEMORY);
        System.out.println(javaHeapTest.getOom().length());

        Thread.sleep(100000);
    }
}
