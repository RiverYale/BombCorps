package com.bombcorps.game.model;

import java.io.Serializable;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.NetController;

public class Message implements Serializable{
    private int msg;
//    private String ID;

    private String fromIp;
    private String toIp;

    private String mapName;
    private Player player;

    private boolean hasBonus;





    /*
    房间内选项
     */
    private int heroChoosed;   //选择的英雄
    private String mapNameChoosed;  //选择的地图
    private int sideChoosed;    //选择的阵营
    /*
    游戏内行为
    */
    private int heroAction;   //英雄行为
    //移动信息
    private int destination;    //英雄移动的目的地
    //发射炸弹信息
    private Vector2 bombVelocity;  //炸弹发射的速度
    private Vector2 bombStartPosition;//炸弹发射的坐标






    public Message(int msg){
        this.msg = msg;
        this.fromIp = NetController.getLocalHostIp();
    }

    public String getToIp() {
        return toIp;
    }

    public void setToIp(String toIp) {
        this.toIp = toIp;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void setHeroChoosed(int heroChoosed){
        this.heroChoosed = heroChoosed;
    }

    public void setMapNameChoosed(String mapName){
        this.mapNameChoosed = mapName;
    }

    public void setSideChoosed(int side){
        sideChoosed = side;
    }

    public void setHeroAction(int action){
        heroAction = action;
    }

    public int getMsg() {
        return msg;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getMap() {
        return mapName;
    }

    public void setMap(String mapName) {
        this.mapName = mapName;
    }

    public boolean hasBonus() {
        return hasBonus;
    }

    public void setBonus(boolean hasBonus) {
        this.hasBonus = hasBonus;
    }

    public int getHeroChoosed() {
        return heroChoosed;
    }

    public String getMapNameChoosed() {
        return mapNameChoosed;
    }

    public int getSideChoosed() {
        return sideChoosed;
    }

    public int getHeroAction() {
        return heroAction;
    }
}
