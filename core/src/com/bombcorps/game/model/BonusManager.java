package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class BonusManager {
    private Array<Bonus> bonusList;
    private int mapWidth;

    public BonusManager(int mapWidth){
        bonusList = new Array<Bonus>();
        this.mapWidth = mapWidth;
    }

    public void setBonusByChance(){
        if(Math.random() > Constants.BONUS.BONUS_CHANCE)
            return;

        bonusList.add(new Bonus(mapWidth));
    }

    public void update(float deltaTime){
        for(Bonus i : bonusList){
            i.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch){
        for(Bonus i : bonusList){
            i.render(batch);
        }
    }

    public Array<Bonus> getBonusList(){
        return bonusList;
    }

    public void deleteIndexAtIndex(int index){
        if(index < bonusList.size)
            bonusList.removeIndex(index);
        else
            Gdx.app.error("Out of Bounds : ", "BonusList At deleteIndexAtIndex");
    }

}

