package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.model.bombs.Bomb;
import com.bombcorps.game.model.heros.Angel;
import com.bombcorps.game.model.heros.BaseHero;
import com.bombcorps.game.model.heros.Protector;
import com.bombcorps.game.model.heros.Sniper;
import com.bombcorps.game.model.heros.Sparda;
import com.bombcorps.game.model.heros.Wizard;
import com.sun.jndi.cosnaming.CNCtx;

public class PlayerManager {

    private SkillAndBuff skillAndBuff;

    private Array<Player> playerListRed;
    private Array<Player> playerListBlue;



    private Bomb bomb;   //TODO

    public PlayerManager(){
        playerListRed = new Array<Player>();
        playerListBlue = new Array<Player>();
    }

    public void addPlayer(String IP, int team, String ID){
        Player player = new Player(ID);
        player.setTeam(team);
        player.setIp(IP);

        if(team == Constants.PLAYER.RED_TEAM)
            playerListRed.add(player);
        else
            playerListBlue.add(player);
    }

    public void deletePlayerAtIndex(int index, int side){
        if(side == Constants.PLAYER.BLUE_TEAM) {
            if (index < playerListRed.size)
                playerListRed.removeIndex(index);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"deletePlayerAtIndex\"");
        }
        else {
            if (index < playerListBlue.size)
                playerListBlue.removeIndex(index);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"deletePlayerAtIndex\"");
        }

    }

    public void gameBegin(){
        bomb = new Bomb();
//        buff = new Buff();
        skillAndBuff = new SkillAndBuff(playerListBlue, playerListRed);

        for(Player i : playerListRed)
            i.creatHero(skillAndBuff, bomb);
        for(Player i : playerListBlue)
            i.creatHero(skillAndBuff, bomb);
    }

    public void update(float deltaTime){
        for(Player i : playerListRed)
            i.getMyHero().update(deltaTime);

        for(Player i : playerListBlue)
            i.getMyHero().update(deltaTime);
    }

    public void render(SpriteBatch batch){
        for(Player i : playerListRed)
            i.getMyHero().render(batch);

        for(Player i : playerListBlue)
            i.getMyHero().render(batch);
    }


    public void explode(Player player){
        bomb.setFromPlayer(player);
        bomb.explode(playerListRed, playerListBlue);
    }

    public Array<Player> getAllPlayerList(){
        Array<Player> list = new Array<Player>();
        list.addAll(playerListRed);
        list.addAll(playerListBlue);

        return list;
    }

    public Array<Player> getRedPlayerList(){
        return playerListRed;
    }

    public Array<Player> getBluePlayerList(){
        return playerListBlue;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setPlayerReadyAtIndex(int index, boolean ready, int team){
        if(team == Constants.PLAYER.RED_TEAM) {
            if (index < playerListRed.size)
                playerListRed.get(index).setReady(ready);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerReadyAtIndex\"");
        }
        else{
            if(index < playerListBlue.size)
                playerListBlue.get(index).setReady(ready);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerReadyAtIndex\"");
        }
    }

    public boolean getPlayerReadyAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM) {
            if (index < playerListRed.size)
                return playerListRed.get(index).getReady();

            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerReadyAtIndex\"");
            return false;
        }else{
            if(index < playerListBlue.size)
                return playerListBlue.get(index).getReady();

            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerReadyAtIndex\"");
            return false;
        }

    }


    public void setPlayerHeroTypeAtIndex(int index, int heroType, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                playerListRed.get(index).setHeroType(heroType);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerHeroTypeAtIndex\"");
        }else{
            if(index < playerListBlue.size)
                playerListBlue.get(index).setHeroType(heroType);
            else
                Gdx.app.error("Out of Bounds : ", "PlayerList At function \"setPlayerHeroTypeAtIndex\"");
        }
    }

    public int getPlayerHeroTypeAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                return playerListRed.get(index).getHeroType();

            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerHeroTypeAtIndex\"");
            return -1;
        }else{
            if(index < playerListBlue.size)
                return playerListBlue.get(index).getHeroType();

            Gdx.app.error("Out of Bounds : ", "PlayerList At function \"getPlayerHeroTypeAtIndex\"");
            return -1;
        }
    }

    public void changePlayerSideAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size){
                Player temp = playerListRed.removeIndex(index);
                temp.setTeam(Constants.PLAYER.BLUE_TEAM);
                playerListBlue.add(temp);
            }else{
                Gdx.app.error("Out of Bounds : ", "playerList in function \"setPlayerSideAtIndex\"");
            }
        }else{
            if(index < playerListBlue.size){
                Player temp = playerListBlue.removeIndex(index);
                temp.setTeam(Constants.PLAYER.RED_TEAM);
                playerListRed.add(temp);
            }else{
                Gdx.app.error("Out of Bounds : ", "playerList in function \"setPlayerSideAtIndex\"");
            }

        }

    }

    public Array<Player> getPlayerListRed() {
        return playerListRed;
    }

    public Array<Player> getPlayerListBlue() {
        return playerListBlue;
    }

    public void setPlayerStateAtIndex(int index, int state, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                playerListRed.get(index).setState(state);
            else
                Gdx.app.error("Out Of Bounds : ", "playerList in function \"setPlayerStateAtIndex\" ");
        }else{
            if(index < playerListBlue.size)
                playerListBlue.get(index).setState(state);
            else
                Gdx.app.error("Out Of Bounds : ", "playerList in function \"setPlayerStateAtIndex\" ");
        }

    }

    public int getPlayerStateAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                return playerListRed.get(index).getState();

            Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerStateAtIndex\" ");
            return -1;
        }else{
            if(index < playerListBlue.size)
                return playerListBlue.get(index).getState();

            Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerStateAtIndex\" ");
            return -1;
        }
    }

    public String getPlayerIpAtIndex(int index, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                return playerListRed.get(index).getIp();

            Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerPortAtIndex\" ");
            return null;
        }else{
            if(index < playerListBlue.size)
                return playerListBlue.get(index).getIp();

            Gdx.app.error("Out Of Bounds : ", "playerList in function \"getPlayerPortAtIndex\" ");
            return null;
        }
    }

    public void setPlayerPortAtIndex(int index, String IP, int team){
        if(team == Constants.PLAYER.RED_TEAM){
            if(index < playerListRed.size)
                playerListRed.get(index).setIp(IP);
            else
                Gdx.app.log("Out Of Bounds : ", "playerList in function \"setPlayerIpAtIndex\" ");
        }else{
            if(index < playerListBlue.size)
                playerListBlue.get(index).setIp(IP);
            else
                Gdx.app.log("Out Of Bounds : ", "playerList in function \"setPlayerIpAtIndex\" ");
        }
    }
}
