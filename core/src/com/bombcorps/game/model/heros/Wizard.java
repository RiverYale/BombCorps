package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Wizard extends BaseHero {
    public Wizard(int level) {
        super(Constants.WIZARD);
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

        this.setMaxHealth(Constants.Wizard.HEALTH * getLevel());
        this.setHealth(Constants.Wizard.HEALTH * getLevel());
        this.setEndurance(200f);
        this.setRagePower(0);

        this.setAttack(Constants.Wizard.ATTACK * getLevel());
        this.setCriticalProbability(Constants.Wizard.START_CRITICALPROBABILITY);
        this.setArmor(Constants.Wizard.ARMOR * getLevel());
    }
}
