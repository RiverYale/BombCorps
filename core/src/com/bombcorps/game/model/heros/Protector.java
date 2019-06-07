package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Protector extends BaseHero {
    public Protector(int level) {
        super(Constants.PROTECTOR);
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

        this.setMaxHealth(Constants.Protector.HEALTH * getLevel());
        this.setHealth(Constants.Protector.HEALTH * getLevel());
        this.setEndurance(200f);
        this.setRagePower(0);

        this.setAttack(Constants.Protector.ATTACK * getLevel());
        this.setCriticalProbability(Constants.Protector.START_CRITICALPROBABILITY);
        this.setArmor(Constants.Protector.ARMOR * getLevel());
    }
}
