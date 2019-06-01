package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Sparda extends BaseHero {

    public Sparda() {
        super(Constants.SPARDA);
        init();
    }

    private void init(){
        this.setMaxHealth(Constants.Sparda.HEALTH * getLevel());
        this.setHealth(Constants.Sparda.HEALTH * getLevel());
        this.setEndurance(200f);
        this.setRagePower(100f);

        this.setAttack(Constants.Sparda.ATTACK * getLevel());
        this.setCriticalProbability(Constants.Sparda.START_CRITICALPROBABILITY);
        this.setArmor(Constants.Sparda.ARMOR * getLevel());
    }
}
