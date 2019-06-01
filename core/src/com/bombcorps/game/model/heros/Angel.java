package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Angel extends BaseHero {

    public Angel(){
        super(Constants.ANGEL);
        init();
    }

    private void init(){
        this.setMaxHealth(Constants.Angel.HEALTH * getLevel());
        this.setHealth(Constants.Angel.HEALTH * getLevel());
        this.setEndurance(200f);
        this.setRagePower(100f);

        this.setAttack(Constants.Angel.ATTACK * getLevel());
        this.setCriticalProbability(Constants.Angel.START_CRITICALPROBABILITY);
        this.setArmor(Constants.Angel.ARMOR * getLevel());

    }



}
