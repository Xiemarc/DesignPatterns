package com.xie.designpatterns.design.duty;

/**
 * Created by marc on 2017/6/15.
 */
public class Client {
    public static void main(String[] args) {
        //责任链式模式，需要组装链表
        Handler handler1 = new Handler1();
        Handler handler2 = new Handler2();
        Handler handler3 = new Handler3();
        //拼接链子
        handler1.nextHandler = handler2;
        handler2.nextHandler = handler3;
        AbstractRequest request = new Request2();
        //一定要将请求对象丢给第一个对象,不然没办法做责任链式的传递
        handler1.handRequest(request);
    }
}
