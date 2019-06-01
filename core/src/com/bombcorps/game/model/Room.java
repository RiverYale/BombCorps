package com.bombcorps.game.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bombcorps.game.controller.AssetsController;

public class Room {
    private String ownerIp;

    private TextureRegion mapRegion;    //地图
    private TextureRegion[] heroRegion; //英雄头像
    private String mapName;

    private PlayerManager playerManager;

    private int LIMIT;

    public Room(String ownerIp, int limit){
        init(ownerIp);
        this.LIMIT = limit;
    }

    public void addPlayer(Player player){
        add(player.getIp(), player.getID());
    }

    private void add(String IP, String ID){        //优先加入红色
        if(playerManager.getRedPlayerList().size < LIMIT){
            playerManager.addPlayer(IP, Constants.PLAYER.RED_TEAM, ID);
        }else if(playerManager.getBluePlayerList().size < LIMIT){
            playerManager.addPlayer(IP, Constants.PLAYER.BLUE_TEAM, ID);
        }
    }

    public void removePlayer(Player player){
        if(player.getTeam() == Constants.PLAYER.RED_TEAM){
            for(int i = 0 ; i < playerManager.getRedPlayerList().size ; i++){
                if(player.getIp().equals(playerManager.getPlayerListRed().get(i).getIp())){
                    playerManager.getRedPlayerList().removeIndex(i);
                    break;
                }
            }
        }else{
            for(int i = 0 ; i < playerManager.getBluePlayerList().size ; i++){
                if(player.getIp().equals(playerManager.getPlayerListBlue().get(i).getIp())){
                    playerManager.getBluePlayerList().removeIndex(i);
                    break;
                }
            }
        }
    }

    public void updatePlayer(Player player){
        for(int i = 0 ; i < playerManager.getRedPlayerList().size ; i++){
            if(player.getIp().equals(playerManager.getPlayerListRed().get(i).getIp())){
                playerManager.getRedPlayerList().set(i,player);
                break;
            }
        }

        for(int i = 0 ; i < playerManager.getBluePlayerList().size ; i++){
            if(player.getIp().equals(playerManager.getPlayerListBlue().get(i).getIp())){
                playerManager.getBluePlayerList().set(i,player);
                break;
            }
        }
    }



    public void addAi(){

    }

    public void switchTeam(Player player){
        /*
        更换房间
         */
        if(player.getTeam() == Constants.PLAYER.RED_TEAM){
            for(int i = 0 ; i < playerManager.getRedPlayerList().size ; i++){
                if(player.getIp().equals(playerManager.getPlayerListRed().get(i).getIp())){
                    if(playerManager.getBluePlayerList().size < LIMIT){
                        playerManager.getBluePlayerList().add(playerManager.getRedPlayerList().get(i));
                        playerManager.getRedPlayerList().removeIndex(i);
                    }

                }
            }
        }else{
            for(int i = 0 ; i < playerManager.getBluePlayerList().size ; i++){
                if(player.getIp().equals(playerManager.getPlayerListBlue().get(i).getIp())){
                    if(playerManager.getRedPlayerList().size < LIMIT){
                        playerManager.getRedPlayerList().add(playerManager.getBluePlayerList().get(i));
                        playerManager.getBluePlayerList().removeIndex(i);
                    }
                }
            }
        }

    }


    private void init(String onwerIp){
        this.ownerIp = ownerIp;
        heroRegion = new TextureRegion[6];
        heroRegion[0] = AssetsController.instance.getRegion("Sparda0");
        heroRegion[1] = AssetsController.instance.getRegion("Protector0");
        heroRegion[2] = AssetsController.instance.getRegion("Angel0");
        heroRegion[3] = AssetsController.instance.getRegion("Sniper0");
        heroRegion[4] = AssetsController.instance.getRegion("Wizard0");

        playerManager = new PlayerManager();
    }

    public boolean isFull(){
        if(playerManager.getRedPlayerList().size == LIMIT && playerManager.getBluePlayerList().size == LIMIT){
            return true;
        }

        return false;
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



    public String getOwnerIp(){
        return ownerIp;
    }

    public void setOwnerIp(String Ip){
        this.ownerIp = Ip;
    }

    public void setMapRegion(TextureRegion mapRegion){
        this.mapRegion = mapRegion;
    }

    public TextureRegion getMapRegion(){
        return mapRegion;
    }
}
