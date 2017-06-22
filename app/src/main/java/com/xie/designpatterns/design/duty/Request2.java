package com.xie.designpatterns.design.duty;

/**
 * 具体的请求2
 * Created by marc on 2017/6/15.
 */

public class Request2 extends AbstractRequest{
    @Override
    public int getRequestLevel() {
        return 2;
    }
}
