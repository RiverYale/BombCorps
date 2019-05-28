package com.bombcorps.game.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bombcorps.game.controller.AssetsController;

public class Room {
    private int ownerPort;

    private TextureRegion mapRegion;    //地图
    private TextureRegion[] heroRegion; //英雄头像
    private String mapName;

    private PlayerManager playerManager;

    public Room(int ownerPort){
        init(ownerPort);
    }

    private void init(int ownerPort){
        this.ownerPort = ownerPort;
        heroRegion = new TextureRegion[6];
        heroRegion[0] = AssetsController.instance.getRegion("Sparda0");
        heroRegion[1] = AssetsController.instance.getRegion("Protector0");
        heroRegion[2] = AssetsController.instance.getRegion("Angel0");
        heroRegion[3] = AssetsController.instance.getRegion("Sniper0");
        heroRegion[4] = AssetsController.instance.getRegion("Wizard0");

        playerManager = new PlayerManager();
    }

    public PlayerManager getPlayerManager(){
        return playerManager;
    }

    public void setMapName(String mapName){
        this.mapName = mapName;
    }

    public String getMapName(){
        return mapName;
    }

    public void setMapRegion(TextureRegion mapRegion){
        this.mapRegion = mapRegion;
    }

    public TextureRegion getMapRegion(){
        return mapRegion;
    }
}
