package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.AudioController;
import com.bombcorps.game.controller.CameraController;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.bombs.Bomb;
import com.bombcorps.game.model.heros.*;

import java.io.Serializable;
import java.util.Set;

public class Player implements Serializable {
    private String ID;
    private BaseHero myHero;
    private int heroType;
    private String IP;
    private int level;

    private Vector2 tap;

    private boolean current;
    private boolean ready;

    public SkillAndBuff skillAndBuff;
    public Bomb bomb;

    private TEAM team;
    public enum TEAM{
        RED,BLUE
    }

    private STATE state;
    public enum STATE{
        LOCAL, OTHERS
    }

    public Player(String ID){
        ready = false;
        this.ID = ID;
        heroType = Constants.SPARDA;
        tap = new Vector2(0, 0);
    }

    public Player(Message.MPlayer m) {
        ID = m.ID;
        IP = m.IP;
        heroType = m.heroType;
        level = m.level;
        ready = m.ready;
        team = m.team;
        state = m.state;
        tap = new Vector2(0, 0);
    }

    public Message.MPlayer getMPlayer() {
        return new Message.MPlayer(ID, IP, heroType, level, ready, team, state);
    }

    public void creatHero(SkillAndBuff skillAndBuff, Bomb bomb){
        this.skillAndBuff = skillAndBuff;
        this.bomb = bomb;

        switch (heroType){
            case Constants.PROTECTOR:
                myHero = new Protector(level);
                break;
            case Constants.ANGEL:
                myHero = new Angel(level);
                break;
            case Constants.SNIPER:
                myHero = new Sniper(level);
                break;
            case Constants.SPARDA:
                myHero = new Sparda(level);
                break;
            case Constants.WIZARD:
                myHero = new Wizard(level);
                break;
        }

    }

    public void initHeroEveryRound(){       //每回合调用
        /*
        每一回合结束都要把英雄的精力值，怒气值调正初始化
         */
        boolean isweaked = false;
        if(team == TEAM.RED){
            for(int i = 0 ; i < skillAndBuff.playerListRed.size ; i++){
                if(IP.equals(skillAndBuff.playerListRed.get(i).getIp())){
                    if(skillAndBuff.redBuffs.get(i).angel_skill_2_debuff > 0){
                        isweaked =  true;
                    }
                }
            }
        }else{
            for(int i = 0 ; i < skillAndBuff.playerListBlue.size ; i++){
                if(IP.equals(skillAndBuff.playerListBlue.get(i).getIp())){
                    if(skillAndBuff.blueBuffs.get(i).angel_skill_2_debuff > 0){
                        isweaked =  true;
                    }
                }
            }
        }

        if(isweaked) {
            myHero.setEndurance(Constants.MAX_ENDURENCE - Constants.Angel.SKILL_2_ENDURANCE_MIN);
        }else{
            myHero.setEndurance(Constants.MAX_ENDURENCE);
        }

        myHero.setRagePower(MathUtils.clamp(myHero.getRagePower() + Constants.RAGEPOWER_ADD_PER_ROUND,
                0, Constants.MAX_RAGEPOWER));
    }

    public float getDestX(){
        return myHero.getDestination();
    }

    public void setDestX(float destination){
        myHero.setDestination(destination);

        if(team == TEAM.RED){   //如果禁锢
            for(int i = 0 ; i < skillAndBuff.redBuffs.size ; i++){
                if(getIp().equals(skillAndBuff.playerListRed.get(i).getIp())){
                    if(skillAndBuff.redBuffs.get(i).wizard_skill_2_debuff > 0)
                        return;
                }
            }
        }else{
            for(int i = 0 ; i < skillAndBuff.blueBuffs.size ; i++){
                if(getIp().equals(skillAndBuff.playerListBlue.get(i).getIp())){
                    if(skillAndBuff.blueBuffs.get(i).wizard_skill_2_debuff > 0)
                        return;
                }
            }
        }
        myHero.setState(Constants.STATE_MOVING);
    }

    public void setTap(Vector2 tap){
        this.tap = tap;
        if(tap.x > myHero.getPosition().x){
            myHero.setDirection(false);
        }else{
            myHero.setDirection(true);
        }
        bomb.setVelocity(new Vector2(myHero.getPosition().x - tap.x, myHero.getPosition().y - tap.y));
    }

    public Vector2 getTap(){
        return tap;
    }

    public void shoot(CameraController controller){
        controller.setTarget(bomb);
    public void shoot(){
        switch (heroType){
            case Constants.PROTECTOR:
                AudioController.instance.play(AssetsController.instance.protectorshoot);
                break;
            case Constants.ANGEL:
                AudioController.instance.play(AssetsController.instance.angelshoot);
                break;
            case Constants.SNIPER:
                AudioController.instance.play(AssetsController.instance.snipershoot);
                break;
            case Constants.SPARDA:
                AudioController.instance.play(AssetsController.instance.spardashoot);
                break;
            case Constants.WIZARD:
                AudioController.instance.play(AssetsController.instance.angelshoot);
                break;

        }

        bomb.setFromPlayer(this);

        bomb.setState(Constants.BOMB.STATE_FLY);
        myHero.setState(Constants.STATE_ATTACK);
    }

    public boolean useSkill(int op){
        switch (op){
            case 1:
                if(team == TEAM.RED){   //如果禁锢
                    for(int i = 0 ; i < skillAndBuff.redBuffs.size ; i++){
                        if(getIp().equals(skillAndBuff.playerListRed.get(i).getIp())){
                            if(skillAndBuff.redBuffs.get(i).wizard_skill_2_debuff > 0)
                                return false;
                        }
                    }
                }else{
                    for(int i = 0 ; i < skillAndBuff.blueBuffs.size ; i++){
                        if(getIp().equals(skillAndBuff.playerListBlue.get(i).getIp())){
                            if(skillAndBuff.blueBuffs.get(i).wizard_skill_2_debuff > 0)
                                return false;
                        }
                    }
                }

                if(!skillAndBuff.canJump)   //已经非过一次
                    return false;

                if(team == TEAM.RED)
                    skillAndBuff.jump(Constants.PLAYER.RED_TEAM, IP);
                else
                    skillAndBuff.jump(Constants.PLAYER.BLUE_TEAM, IP);

                return true;
            case 2:
                if(myHero.getAttackTimes() == 0)
                    return false;

                return true;
            default:
                heroUseSkillOp(op);
                return false;
        }
    }

    private void heroUseSkillOp(int op){
        switch(heroType){
            case Constants.ANGEL:
                switch (op){
                    case 3:
                        if(team == TEAM.RED)
                            skillAndBuff.angelSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.angelSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                    case 4:
                        if(team == TEAM.RED)
                            skillAndBuff.angelSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.angelSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                    case 5:
                        if(team == TEAM.RED)
                            skillAndBuff.angelSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.angelSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                }
                break;

            case Constants.SPARDA:
                switch (op){
                    case 3:
                        if(team == TEAM.RED){
                            skillAndBuff.spardaSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        }
                        else{
                            skillAndBuff.spardaSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        }
                        break;
                    case 4:
                        if(team == TEAM.RED)
                            skillAndBuff.spardaSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.spardaSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                    case 5:
                        if(team == TEAM.RED)
                            skillAndBuff.spardaSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.spardaSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                }
                break;

            case Constants.SNIPER:
                switch (op){
                    case 3:
                        if(team == TEAM.RED)
                            skillAndBuff.sniperSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.sniperSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                    case 4:
                        if(team == TEAM.RED)
                            skillAndBuff.sniperSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.sniperSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                    case 5:
                        if(team == TEAM.RED)
                            skillAndBuff.sniperSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.sniperSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                }
                break;

            case Constants.PROTECTOR:
                switch (op){
                    case 3:
                        if(team == TEAM.RED)
                            skillAndBuff.protectorSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.protectorSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                    case 4:
                        if(team == TEAM.RED)
                            skillAndBuff.protectorSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.protectorSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                    case 5:
                        if(team == TEAM.RED)
                            skillAndBuff.protectorSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.protectorSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                }
                break;

            case Constants.WIZARD:
                switch (op){
                    case 3:
                        if(team == TEAM.RED)
                            skillAndBuff.wizardSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.wizardSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                    case 4:
                        if(team == TEAM.RED)
                            skillAndBuff.wizardSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.wizardSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                    case 5:
                        if(team == TEAM.RED)
                            skillAndBuff.wizardSkill.useSkill_1(Constants.PLAYER.RED_TEAM, IP);
                        else
                            skillAndBuff.wizardSkill.useSkill_1(Constants.PLAYER.BLUE_TEAM, IP);
                        break;
                }
                break;

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

    public Rectangle getRect(){
        return myHero.getRec();
    }

    public Bomb getBomb(){
        return bomb;
    }

    public Vector2 getPosition(){
        return myHero.getPosition();
    }

    public void setX(float input){
        myHero.setPosition(new Vector2(input, myHero.getPosition().y));
    }

    public void setY(float input){
        myHero.setPosition(new Vector2(myHero.getPosition().x, input));
    }

    public int getHeroState(){
        return myHero.getState();
    }

    public void setHeroState(int input){
        myHero.setState(input);
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
