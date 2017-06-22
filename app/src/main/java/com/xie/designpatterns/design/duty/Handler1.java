package com.xie.designpatterns.design.duty;

/**
 * 具体处理者1
 * Created by marc on 2017/6/15.
 */

public class Handler1 extends Handler {
    @Override
    public int getHandlerLevel() {
        return 1;
    }

    @Override
    public void handler(AbstractRequest request) {
        System.out.println("handler1-->①能够处理对象" + request.getRequestLevel());
    }
}
