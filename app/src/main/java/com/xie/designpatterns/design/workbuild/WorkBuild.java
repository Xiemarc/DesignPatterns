package com.xie.designpatterns.design.workbuild;

/**
 * 建造者模式--建造
 * Created by marc on 2017/6/15.
 */

public class WorkBuild {
    private RoomPrarms params;

    public WorkBuild() {
        this.params = new RoomPrarms();
    }

    public WorkBuild makeWindow(String window) {
        params.window = window;
        return this;
    }

    public WorkBuild makeFloor(String floor) {
        params.floor = floor;
        return this;
    }

    public WorkBuild makeDoor(String door) {
        params.door = door;
        return this;
    }

    public Room makeChat(String chat) {
        params.chat = chat;
        Room room = new Room();
        return room;
    }


    /**
     * 内部类，每个房间的属相
     */
    class RoomPrarms {
        public String window;
        public String floor;
        public String door;
        public String chat;
    }
}
