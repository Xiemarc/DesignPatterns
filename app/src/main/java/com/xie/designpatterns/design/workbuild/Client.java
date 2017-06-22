package com.xie.designpatterns.design.workbuild;

/**
 * Created by marc on 2017/6/15.
 */

public class Client {

    public static void main(String[] args) {
        Room room=(new WorkBuild()).makeWindow("蓝色玻璃").makeFloor("黄色地板").makeChat("");
        System.out.println(room);
    }
}
