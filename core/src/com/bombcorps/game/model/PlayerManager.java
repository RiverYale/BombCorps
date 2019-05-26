package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class PlayerManager {
    private Array<Player> playerList;

    public PlayerManager(){
        playerList = new Array<Player>();
    }

    public void addPlayer(int port, int side, String ID, float[] level){
        Player player = new Player(ID);
        player.setLevel(level);
        player.setPort(port);
        player.setSide(side);

        playerList.add(player);
    }

    public void deletePlayerAtIndex(int index){
        if(index < playerList.size)
            playerList.removeIndex(index);
        else
            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"deletePlayerAtIndex\"");
    }

    public void creatHeros(){
        for(Player i : playerList){
            i.creatHero();
        }
    }

    public void update(float deltaTime){
        for(Player i : playerList){
            i.getMyHero().update(deltaTime);
        }
    }

    public void render(SpriteBatch batch){
        for(Player i : playerList){
            i.getMyHero().render(batch);
        }
    }

    public void setPlayerReadyAtIndex(int index, boolean ready){
        if(index < playerList.size)
            playerList.get(index).setReady(ready);
        else
            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerReadyAtIndex\"");
    }

    public boolean getPlayerReadyAtIndex(int index){
        if(index < playerList.size)
            return playerList.get(index).getReady();


        Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerReadyAtIndex\"");
        return false;
    }

    public void setPlayerLevelAtIndex(int index, float[] level){
        if(index < playerList.size)
            playerList.get(index).setLevel(level);
        else
            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerLevelAtIndex\"");
    }

    public float[] getPlayerLevelAtIndex(int index){
        if(index < playerList.size)
            return playerList.get(index).getLevel();

        Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerLevelAtIndex\"");
        return null;
    }

    public void setPlayerHeroTypeAtIndex(int index, int heroType){
        if(index < playerList.size)
            playerList.get(index).setHeroType(heroType);
        else
            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerHeroTypeAtIndex\"");
    }

    public int getPlayerHeroTypeAtIndex(int index){
        if(index < playerList.size)
            return playerList.get(index).getHeroType();

        Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerHeroTypeAtIndex\"");
        return -1;
    }

    public void setPlayerSideAtIndex(int index, int side){
        if(index < playerList.size)
            playerList.get(index).setSide(side);
        else{
            Gdx.app.error("Out of Bounds : ", "playerList in function \"setPlayerSideAtIndex\"");
        }
    }

    public int getPlayerSideAtIndex(int index){
        if(index < playerList.size)
            return playerList.get(index).getSide();

        Gdx.app.error("Out of Bounds : ", "playerList in function \"getPlayerSideAtIndex\"");
        return -1;
    }

    public Array<Player> getPlayerList(){
        return playerList;
    }

    public void setPlayerStateAtIndex(int index, int state){
        if(index < playerList.size)
            playerList.get(index).setState(state);
        else
            Gdx.app.error("Out Of Bounds : ", "playerList in function \"setPlayerStateAtIndex\" ");
    }

    public int getPlayerStateAtIndex(int index){
        if(index < playerList.size)
            return playerList.get(index).getState();

        Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerStateAtIndex\" ");
        return -1;
    }

    public int getPlayerPortAtIndex(int index){
        if(index < playerList.size)
            return playerList.get(index).getPort();

        Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerPortAtIndex\" ");
        return -1;
    }

    public void setPlayerPortAtIndex(int index, int port){
        if(index < playerList.size)
            playerList.get(index).setPort(port);
        else
            Gdx.app.log("Out Of Bounds : ", "playerList in function \"setPlayerPortAtIndex\" ");
    }
}
