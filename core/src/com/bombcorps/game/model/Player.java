package com.bombcorps.game.model;

import com.bombcorps.game.model.auras.Aura;
import com.bombcorps.game.model.heros.*;

public class Player {
    private BaseHero myHero;
    private int port;
    private STATE state;

    private enum STATE{
        LOCAL, OTHERS
    }

    public Player(int heroType){
        switch (heroType){
            case Constants.PROTECTOR:
                myHero = new Protector();
                break;
            case Constants.ANGEL:
                myHero = new Angel();
                break;
            case Constants.SNIPER:
                myHero = new Sniper();
                break;
            case Constants.SPARDA:
                myHero = new Sparda();
                break;
            case Constants.WIZARD:
                myHero = new Wizard();
                break;
        }
    }

    public BaseHero getMyHero(){
        return myHero;
    }

    public void setPort(int port){
        this.port = port;
    }

    public int getPort(){
        return port;
    }

    public void setState(int input){
        switch(input){
            case Constants.PLAYER.STATE_LOCAL:
                state = STATE.LOCAL;
                break;
            case Constants.PLAYER.STATE_OTHERS:
                state = STATE.OTHERS;
                break;
        }
    }

    public int getState(){
        if(state == STATE.LOCAL)
            return Constants.PLAYER.STATE_LOCAL;

        return Constants.PLAYER.STATE_OTHERS;
    }

}
