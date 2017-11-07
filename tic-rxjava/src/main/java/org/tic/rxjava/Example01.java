package org.tic.rxjava;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public class Example01 {

    public static void main(String[] args) {

        /*
         * 这个例子很简单：事件的内容是字符串，而不是一些复杂的对象
         * 事件的内容是已经定义好了的，而不像有的观察者模式一样是待确定的（例如网络请求的结果在请求返回之前是未知的）
         *
         * 所有事件在一瞬间被全部发送出去，而不是夹杂一些确定或不确定的时间间隔或者经过某种触发器来触发的。
         */
        Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //发射一个hello world的string
                subscriber.onNext("Hello, World!");
                //发射完成，这种方法需要手动调用onCompleted，才会回调Observer的onCompleted方法
                subscriber.onCompleted();
            }
        });

        /*
         * 定义Subscriber来处理Observable对象发出的字符串
         */
        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };


        //两者关联
        myObservable.subscribe(mySubscriber);

        Subscription subscription = Observable.just("Hello, World! wakaka")
                .subscribe(System.out::println);

    }

}
