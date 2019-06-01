package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Sniper extends BaseHero {

    public Sniper() {
        super(Constants.SNIPER);
        init();
    }

    private void init(){
        this.setMaxHealth(Constants.Sniper.HEALTH * getLevel());
        this.setHealth(Constants.Sniper.HEALTH * getLevel());
        this.setEndurance(200f);
        this.setRagePower(100f);

        this.setAttack(Constants.Sniper.ATTACK * getLevel());
        this.setCriticalProbability(Constants.Sniper.START_CRITICALPROBABILITY);
        this.setArmor(Constants.Sniper.ARMOR * getLevel());
    }
}
