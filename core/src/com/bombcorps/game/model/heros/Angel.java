package com.bombcorps.game.model.heros;

import com.bombcorps.game.model.Constants;

public class Angel extends BaseHero {
    public Angel(){
        super(Constants.ANGEL);

    }

    protected void init(){
        this.setHealth(1000f);
        this.setArmor(50f);
        this.setAttack(50f);


    }
}
