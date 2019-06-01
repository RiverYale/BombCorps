package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Wizard extends BaseHero {
    public Wizard() {
        super(Constants.WIZARD);
        init();
    }

    private void init(){
        this.setMaxHealth(Constants.Wizard.HEALTH * getLevel());
        this.setHealth(Constants.Wizard.HEALTH * getLevel());
        this.setEndurance(200f);
        this.setRagePower(100f);

        this.setAttack(Constants.Wizard.ATTACK * getLevel());
        this.setCriticalProbability(Constants.Wizard.START_CRITICALPROBABILITY);
        this.setArmor(Constants.Wizard.ARMOR * getLevel());
    }
}
