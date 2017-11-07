package org.tic.rxjava;
/*

RxJava 1.0

Observable：发射源，可观察的，在观察者模式中称为"被观察者"或"可观察对象"

Observer：接收源，观察者，可接收Observable、Subject发射的数据

Subject：是一个比较特殊的对象，既可充当发射源，也可充当接收源

Subscriber：订阅者，也是接收源，跟Observer有什么区别呢？Subscriber实现了Observer接口，比Observer
多了一个最重要的方法unsubscribe()，用来取消订阅。Observer在subscribe()过程中，最终也会转换成Subscriber
对象，一般情况下，建议使用Subscriber作为接收源

Subscription：Observable调用subscribe()方法返回的对象，同样有unsubscribe()方法，可以用来取消订阅事件

Action0：RxJava中的一个借口，

Func0：


RxJava最核心的两个东西Observables和Subscribers。


http://blog.csdn.net/caihongdao123/article/details/51897793
http://www.daidingkang.cc/2017/05/19/Rxjava/

*/