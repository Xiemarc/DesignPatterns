package com.xie.designpatterns.design.command;

/**
 * 圣旨
 * Created by marc on 2017/6/16.
 */

public interface Command {
    /**
     * 打仗
     */
    void excete();

    /**
     * 撤退
     */
    void back();
}
