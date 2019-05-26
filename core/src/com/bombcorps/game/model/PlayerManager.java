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

    public void deletePlayerAt(int index){
        if(index < playerList.size)
            playerList.removeIndex(index);
        else
            Gdx.app.log("Out of Bounds : ", "PlayerList");
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
}
