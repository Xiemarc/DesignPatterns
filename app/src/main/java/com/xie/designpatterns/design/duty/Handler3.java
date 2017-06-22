package com.xie.designpatterns.design.duty;

/**
 * 处理者3
 * Created by marc on 2017/6/15.
 */

public class Handler3 extends Handler{
    @Override
    public int getHandlerLevel() {
        return 3;
    }

    @Override
    public void handler(AbstractRequest request) {
        System.out.println("handler3-->③能够处理对象" + request.getRequestLevel());
    }
}
