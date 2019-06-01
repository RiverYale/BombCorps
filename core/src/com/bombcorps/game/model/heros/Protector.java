package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Protector extends BaseHero {
    public Protector() {
        super(Constants.PROTECTOR);
        init();
    }

    private void init(){
        this.setMaxHealth(Constants.Protector.HEALTH * getLevel());
        this.setHealth(Constants.Protector.HEALTH * getLevel());
        this.setEndurance(200f);
        this.setRagePower(100f);

        this.setAttack(Constants.Protector.ATTACK * getLevel());
        this.setCriticalProbability(Constants.Protector.START_CRITICALPROBABILITY);
        this.setArmor(Constants.Protector.ARMOR * getLevel());
    }
}
