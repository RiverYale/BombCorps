package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class PlayerManager {
    private Array<Player> playerList;

    public PlayerManager(){
        playerList = new Array<Player>();
    }

    public void addPlayer(int heroType, int state, int port){
        Player player = new Player(heroType);
        player.setState(state);
        player.setPort(port);

        playerList.add(player);
    }

    public void deletePlayerAtIndex(int index){
        if(index < playerList.size)
            playerList.removeIndex(index);
        else
            Gdx.app.log("Out of Bounds : ", "PlayerList At function \"deletePlayerAtIndex\"");
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

    public Array<Player> getPlayerList(){
        return playerList;
    }

    public void setPlayerStateAtIndex(int index, int state){
        if(index < playerList.size)
            playerList.get(index).setState(state);
        else
            Gdx.app.log("Out Of Bounds : ", "playerList in function \"setPlayerStateAtIndex\" ");
    }

    public int getPlayerStateAtIndex(int index){
        if(index < playerList.size)
            return playerList.get(index).getState();

        Gdx.app.log("Out Of Bounds : ", "playerList in function \"getPlayerStateAtIndex\" ");
        return -1;
    }

    public int getPlayerPortAtIndex(int index){
        if(index < playerList.size)
            return playerList.get(index).getPort();

        Gdx.app.log("Out Of Bounds : ", "playerList in function \"getPlayerPortAtIndex\" ");
        return -1;
    }

    public void setPlayerPortAtIndex(int index, int port){
        if(index < playerList.size)
            playerList.get(index).setPort(port);
        else
            Gdx.app.log("Out Of Bounds : ", "playerList in function \"setPlayerPortAtIndex\" ");
    }
}
