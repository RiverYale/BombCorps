package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Angel extends BaseHero {

    public Angel(int level){
        super(Constants.ANGEL);
        init(level);
    }

    private void init(int level){
        switch (level){
            case 0:
                this.setLevel(Constants.LEVEL_0);
                break;
            case 1:
                this.setLevel(Constants.LEVEL_1);
                break;
            case 2:
                this.setLevel(Constants.LEVEL_2);
                break;
            case 3:
                this.setLevel(Constants.LEVEL_3);
                break;
            case 4:
                this.setLevel(Constants.LEVEL_4);
                break;
            case 5:
                this.setLevel(Constants.LEVEL_5);
                break;
        }

        this.setMaxHealth(Constants.Angel.HEALTH * getLevel());
        this.setHealth(Constants.Angel.HEALTH * getLevel());
        this.setEndurance(200f);
        this.setRagePower(0);

        this.setAttack(Constants.Angel.ATTACK * getLevel());
        this.setCriticalProbability(Constants.Angel.START_CRITICALPROBABILITY);
        this.setArmor(Constants.Angel.ARMOR * getLevel());

    }



}
