package com.bombcorps.game.model;

import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.bombs.Bomb;
import com.bombcorps.game.model.heros.*;

public class Player {
    private String ID;
    private BaseHero myHero;
    private int heroType;
    private String IP;
    private int level;

    private boolean ready;

    private SkillAndBuff skillAndBuff;
    public Bomb bomb;

    private TEAM team;
    private enum TEAM{
        RED,BLUE
    }

    private STATE state;
    private enum STATE{
        LOCAL, OTHERS
    }

    public Player(String ID){
        ready = false;
        this.ID = ID;
        heroType = Constants.NONE;
    }

    public void creatHero(SkillAndBuff skillAndBuff, Bomb bomb){
        this.skillAndBuff = skillAndBuff;
        this.bomb = bomb;

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

    public void setDestX(float destination){

    }

    public void setTapX(float velocityX){

    }

    public void setTapY(float velocityY){

    }

    public void shoot(){

/*
TODO
 */
        myHero.setState(Constants.STATE_ATTACK);

        bomb.setState(Constants.BOMB.STATE_FLY);
    }

    public void useSkill(int op, Player target){
        switch (op){
            case 3:
//                switch (heroType){
//                    case
//                }
//                skillAndBuff.
        }

    }

    public String getID(){
        return ID;
    }

    public void setTeam(int team){
        switch (team){
            case Constants.PLAYER.BLUE_TEAM:
                this.team = TEAM.BLUE;
                break;
            case Constants.PLAYER.RED_TEAM:
                this.team = TEAM.RED;
                break;
        }
    }

    public int getTeam(){
        if(team == TEAM.BLUE)
            return Constants.PLAYER.BLUE_TEAM;
        else
            return Constants.PLAYER.RED_TEAM;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }

    public void setReady(boolean ready){
        this.ready = ready;
    }

    public boolean getReady(){
        return ready;
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

    public boolean isMe(){
        return NetController.getLocalHostIp().equals(IP);
    }



}
