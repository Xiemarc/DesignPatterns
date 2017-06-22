package com.xie.designpatterns.design.workbuild;

/**
 * 建造者模式房间
 * Created by marc on 2017/6/15.
 */

public class Room {
    private String window;//窗户
    private String floor;//地板
    private String door;//门
    private String chat;//
    public void apply(WorkBuild.RoomPrarms params){
        window = params.window;
        floor=params.floor;
        door = params.door;
        chat = params.chat;
    }

    public void setChat(String chat){
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "---->floor  "+floor+"   window   "+window;
    }
    public void show(){

    }
}
