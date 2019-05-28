package com.bombcorps.game.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bombcorps.game.model.heros.*;

public class Player {
    private String ID;
    private BaseHero myHero;
    private int heroType;
    private String IP;
    private float[] level;

    private boolean ready;

    private SIDE side;
    private enum SIDE {
        LEFT_SIDE, RIGHT_SIDE
    }

    private STATE state;
    private enum STATE{
        LOCAL, OTHERS
    }

    public Player(String ID){
        ready = false;
        this.ID = ID;
        level = new float[5];
        heroType = Constants.NONE;
    }

    public void creatHero(){
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

    public String getID(){
        return ID;
    }

    public void setLevel(float[] level){
        this.level = level;
    }

    public float[] getLevel(){
        return level;
    }

    public void setReady(boolean ready){
        this.ready = ready;
    }

    public boolean getReady(){
        return ready;
    }

    public void setSide(int side){
        switch (side){
            case Constants.PLAYER.LEFT_SIDE:
                this.side = SIDE.LEFT_SIDE;
                break;
            case Constants.PLAYER.RIGHT_SIDE:
                this.side = SIDE.RIGHT_SIDE;
                break;
        }
    }

    public int getSide(){
        if(side == SIDE.LEFT_SIDE)
            return Constants.PLAYER.LEFT_SIDE;

        return Constants.PLAYER.RIGHT_SIDE;
    }

    public void setHeroType(int heroType){
        this.heroType = heroType;
    }

    public int getHeroType(){
        return heroType;
    }

    public BaseHero getMyHero(){
        return myHero;
    }

    public void setIp(String Ip){
        this.IP = Ip;
    }

    public String getIp(){
        return IP;
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
