package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bombcorps.game.controller.AssetsController;

import java.io.Serializable;

public class Room implements Serializable {
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

    public Room(Message.MRoom m) {
        ownerIp = m.ownerIp;
        mapName = m.mapName;
        LIMIT = m.LIMIT;
        heroRegion = new TextureRegion[6];
        heroRegion[0] = AssetsController.instance.getRegion("Sparda_stand");
        heroRegion[1] = AssetsController.instance.getRegion("Protector_stand");
        heroRegion[2] = AssetsController.instance.getRegion("Angel_stand");
        heroRegion[3] = AssetsController.instance.getRegion("Sniper_stand");
        heroRegion[4] = AssetsController.instance.getRegion("Wizard_stand");

        playerManager = new PlayerManager();
        playerManager.addPlayerList(m.playerList);
    }

    public Message.MRoom getMRoom() {
        return new Message.MRoom(ownerIp, mapName, LIMIT, playerManager.getAllPlayerList());
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
                if(player.getIp().equals(playerManager.getRedPlayerList().get(i).getIp())){
                    playerManager.getRedPlayerList().removeIndex(i);
                    break;
                }
            }
        }else{
            for(int i = 0 ; i < playerManager.getBluePlayerList().size ; i++){
                if(player.getIp().equals(playerManager.getBluePlayerList().get(i).getIp())){
                    playerManager.getBluePlayerList().removeIndex(i);
                    break;
                }
            }
        }
    }

    public void updatePlayer(Player player){
        for(int i = 0 ; i < playerManager.getRedPlayerList().size ; i++){
            if(player.getIp().equals(playerManager.getRedPlayerList().get(i).getIp())){
//                playerManager.getRedPlayerList().set(i,player);

                playerManager.getRedPlayerList().get(i).setHeroType(player.getHeroType());
                playerManager.getRedPlayerList().get(i).setState(player.getState());
                playerManager.getRedPlayerList().get(i).setTeam(player.getTeam());
                playerManager.getRedPlayerList().get(i).setReady(player.getReady());
                playerManager.getRedPlayerList().get(i).setLevel(player.getLevel());

                if(player.getTeam() == Constants.PLAYER.BLUE_TEAM){
                    playerManager.getBluePlayerList().add(player);
                    playerManager.getRedPlayerList().removeIndex(i);
                }

                break;
            }
        }

        for(int i = 0 ; i < playerManager.getBluePlayerList().size ; i++){
            if(player.getIp().equals(playerManager.getBluePlayerList().get(i).getIp())){
//                playerManager.getBluePlayerList().set(i,player);
                playerManager.getBluePlayerList().get(i).setHeroType(player.getHeroType());
                playerManager.getBluePlayerList().get(i).setState(player.getState());
                playerManager.getBluePlayerList().get(i).setTeam(player.getTeam());
                playerManager.getBluePlayerList().get(i).setReady(player.getReady());
                playerManager.getBluePlayerList().get(i).setLevel(player.getLevel());

                if(player.getTeam() == Constants.PLAYER.RED_TEAM){
                    playerManager.getRedPlayerList().add(player);
                    playerManager.getBluePlayerList().removeIndex(i);
                }

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
                if(player.getIp().equals(playerManager.getRedPlayerList().get(i).getIp())){
                    if(playerManager.getBluePlayerList().size < LIMIT){
                        playerManager.getBluePlayerList().add(playerManager.getRedPlayerList().get(i));
                        playerManager.getRedPlayerList().get(i).setTeam(Constants.PLAYER.BLUE_TEAM);
                        playerManager.getRedPlayerList().removeIndex(i);
                    }

                }
            }
        }else{
            for(int i = 0 ; i < playerManager.getBluePlayerList().size ; i++){
                if(player.getIp().equals(playerManager.getBluePlayerList().get(i).getIp())){
                    if(playerManager.getRedPlayerList().size < LIMIT){
                        playerManager.getRedPlayerList().add(playerManager.getBluePlayerList().get(i));
                        playerManager.getBluePlayerList().get(i).setTeam(Constants.PLAYER.RED_TEAM);
                        playerManager.getBluePlayerList().removeIndex(i);
                    }
                }
            }
        }

    }


    private void init(String ownerIp){
        this.ownerIp = ownerIp;
        heroRegion = new TextureRegion[6];
        heroRegion[0] = AssetsController.instance.getRegion("Sparda_stand");
        heroRegion[1] = AssetsController.instance.getRegion("Protector_stand");
        heroRegion[2] = AssetsController.instance.getRegion("Angel_stand");
        heroRegion[3] = AssetsController.instance.getRegion("Sniper_stand");
        heroRegion[4] = AssetsController.instance.getRegion("Wizard_stand");

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

    public int getLIMIT(){
        return LIMIT;
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
