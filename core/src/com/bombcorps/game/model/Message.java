package com.bombcorps.game.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.heros.BaseHero;

public class Message implements Serializable{
    private int msg;

    private String fromIp;
    private String toIp;

    private String mapName;
    private MPlayer targetPlayer;
    private MBonus bonus;
    private MRoom room;

    private int op;
    private float targetX;
    private float tapX;
    private float tapY;

    public Message(int msg){
        this.msg = msg;
        this.fromIp = NetController.getLocalHostIp();
    }

    public Room getRoom() {
        return new Room(room);
    }

    public Bonus getBonus() {
        return new Bonus(bonus);
    }

    public void setBonus(Bonus bonus) {
        if (bonus != null) {
            this.bonus = bonus.getMBonus();
        }
    }

    public void setRoom(Room room) {
        this.room = room.getMRoom();
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
        return new Player(targetPlayer);
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer.getMPlayer();
    }

    public String getMap() {
        return mapName;
    }

    public void setMap(String mapName) {
        this.mapName = mapName;
    }

    public static class MBonus implements Serializable{
        public Vector2 position;
        public Vector2 origin;
        public Vector2 scale;
        public Vector2 dimension;
        public Bonus.TYPE type;
        public Bonus.STATE state;

        public MBonus(Vector2 position, Vector2 origin, Vector2 scale, Vector2 dimension, Bonus.TYPE type, Bonus.STATE state) {
            this.position = position;
            this.origin = origin;
            this.scale = scale;
            this.dimension = dimension;
            this.type = type;
            this.state = state;
        }
    }

    public static class MPlayer implements Serializable{
        public String ID;
        public String IP;
        public int heroType;
        public int level;
        public boolean ready;
        public Player.TEAM team;
        public Player.STATE state;

        public MPlayer(String ID, String IP, int heroType, int level, boolean ready, Player.TEAM team, Player.STATE state) {
            this.ID = ID;
            this.IP = IP;
            this.heroType = heroType;
            this.level = level;
            this.ready = ready;
            this.team = team;
            this.state = state;
        }
    }

    public static class MRoom implements Serializable{
        public String ownerIp;
        public String mapName;
        public int LIMIT;
        public ArrayList<MPlayer> playerList;

        public MRoom(String ownerIp, String mapName, int LIMIT, Array<Player> playerList) {
            this.ownerIp = ownerIp;
            this.mapName = mapName;
            this.LIMIT = LIMIT;
            this.playerList = new ArrayList<MPlayer>();
            for (Player p : playerList) {
                this.playerList.add(p.getMPlayer());
                Gdx.app.log("level", ""+p.getLevel());
            }
        }
    }
}
