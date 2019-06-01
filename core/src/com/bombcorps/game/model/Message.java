package com.bombcorps.game.model;

import java.io.Serializable;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.NetController;

public class Message implements Serializable{
    private int msg;

    private String fromIp;
    private String toIp;

    private String mapName;
    private Player targetPlayer;

    private Bonus bonus;

    private int op;
    private float targetX;
    private float tapX;
    private float tapY;

    private Room room;

    public Message(int msg){
        this.msg = msg;
        this.fromIp = NetController.getLocalHostIp();
    }

    public Room getRoom() {
        return room;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setOp(int op, float targetX, float tapX, float tapY){
        this.op = op;
        this.targetX = targetX;
        this.tapX = tapX;
        this.tapY = tapY;
    }

    public int getOp() {
        return op;
    }

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTapX() {
        return tapX;
    }

    public float getTapY() {
        return tapY;
    }

    public String getToIp() {
        return toIp;
    }

    public void setToIp(String toIp) {
        this.toIp = toIp;
    }



    public int getMsg() {
        return msg;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public String getMap() {
        return mapName;
    }

    public void setMap(String mapName) {
        this.mapName = mapName;
    }


}
