package com.bombcorps.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Ai extends Player{

    public float fomerHealth;
    public boolean getHit;
    private boolean GoRight;
    private Array<Player> enemyList;

    public Ai(){
        super("电脑玩家");
    }

    public void setEnemyList(Array<Player> enemyList){  	//游戏开始时调用
        this.enemyList = enemyList;
    }

    public boolean aotuPilot(){                        //到了AI回合，调用此函数
        if(fomerHealth > getMyHero().getHealth()){
            if(GoRight){
                getMyHero().setDestination(getMyHero().getPosition().x + Constants.AI.AIDETINATION);
            }else{
                getMyHero().setDestination(getMyHero().getPosition().x - Constants.AI.AIDETINATION);
            }
            getMyHero().setState(Constants.STATE_MOVING);
        }

        int index = 0;
        for(int i = 0 ; i < enemyList.size ; i++) {     //找到血量最少的一个
            if(enemyList.get(index).getMyHero().getHealth() > enemyList.get(i).getMyHero().getHealth()) {
                index = i;
            }
        }

        while(getMyHero().getState() == Constants.STATE_MOVING);        //等待移动结束


        float velocityY = enemyList.get(index).getPosition().y - getMyHero().getPosition().y;
        velocityY -= Constants.ACCELERATION * (enemyList.get(index).getMyHero().getPosition().x - getMyHero().getPosition().x)*
                        (enemyList.get(index).getMyHero().getPosition().x - getMyHero().getPosition().x) / (2 * Constants.AI.BOMB_VELOCITY_X *
                        Constants.AI.BOMB_VELOCITY_X * Constants.BOMB.ROUTESCALE);
        velocityY *= Constants.AI.BOMB_VELOCITY_X;
        velocityY /= enemyList.get(index).getMyHero().getPosition().x - getMyHero().getPosition().x;

        bomb.setVelocity(new Vector2(Constants.AI.BOMB_VELOCITY_X, velocityY));
        this.shoot();

        return true;
    }
}

