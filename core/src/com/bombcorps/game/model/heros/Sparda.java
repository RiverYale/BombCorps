package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Sparda extends BaseHero {

    public Sparda(int level) {
        super(Constants.SPARDA);
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

        this.setMaxHealth(Constants.Sparda.HEALTH * getLevel());
        this.setHealth(Constants.Sparda.HEALTH * getLevel());
        this.setEndurance(200f);
        this.setRagePower(100f);

        this.setAttack(Constants.Sparda.ATTACK * getLevel());
        this.setCriticalProbability(Constants.Sparda.START_CRITICALPROBABILITY);
        this.setArmor(Constants.Sparda.ARMOR * getLevel());
    }
}
