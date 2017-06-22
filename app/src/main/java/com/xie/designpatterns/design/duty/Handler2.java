package com.xie.designpatterns.design.duty;

/**
 * 具体处理者2
 * Created by marc on 2017/6/15.
 */

public class Handler2 extends Handler {
    @Override
    public int getHandlerLevel() {
        return 2;
    }

    @Override
    public void handler(AbstractRequest request) {
        System.out.println("handler2-->②能够处理对象" + request.getRequestLevel());
    }
}
