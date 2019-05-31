package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Angel extends BaseHero {

    public Angel(){
        super(Constants.ANGEL);
    }

    protected void init(){
        this.setMaxHealth(1000f * getLevel());
        this.setHealth(1000f * getLevel());
        this.setArmor(50f * getLevel());
        this.setAttack(50f * getLevel());
        this.setCriticalProbability(0.2f);
        this.setEndurance(200f);
        this.setRagePower(100f);
        this.setState(Constants.STATE_WAIT);
    }



}
