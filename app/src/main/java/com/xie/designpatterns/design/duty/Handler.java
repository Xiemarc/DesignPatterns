package com.xie.designpatterns.design.duty;

/**
 * 抽象的处理类
 * Created by marc on 2017/6/15.
 */
public abstract class Handler {

    /**
     * 责任链模式的重点。必须持有下一个对象引用
     */
    public Handler nextHandler;

    /**
     * 面向请求的具体处理方法
     */
    public void handRequest(AbstractRequest request) {
        if (request.getRequestLevel() == getHandlerLevel()) {
            //交给具体的类处理
            handler(request);
        } else {
            if (null != nextHandler) {
                nextHandler.handRequest(request);
            } else {
                System.out.println("所有对象都不能处理");
            }
        }
    }

    /**
     * 能够处理请求的级别
     *
     * @return
     */
    public abstract int getHandlerLevel();

    /**
     * 具体的处理方法，交由子类实现
     *
     * @param request
     */
    public abstract void handler(AbstractRequest request);
}
