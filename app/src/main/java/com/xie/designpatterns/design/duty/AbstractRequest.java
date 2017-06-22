package com.xie.designpatterns.design.duty;

/**
 * 抽象的请求类
 * Created by marc on 2017/6/15.
 */
public abstract class AbstractRequest {
    private Object object;
//    public AbstractRequest(Object object){
//        this.object = object;
//    }

    /**
     * 具体的内容对象
     * @return
     */
    public Object getContent(){
        return object;
    }

    /**
     * 获取请求级别
     * @return
     */
    public abstract int getRequestLevel();
}
