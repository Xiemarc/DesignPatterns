package com.xie.designpatterns.design.duty;

/**
 * 具体的请求1 ,
 * 只能被handler1处理，不能被其他handler处理
 * Created by marc on 2017/6/15.
 */

public class Request1 extends AbstractRequest {


    @Override
    public int getRequestLevel() {
        return 1;
    }
}
