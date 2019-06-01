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

    public void addBonus(int type){             //输入bonus的类别，产生该种bonus,位置随机

        Bonus bonus = new Bonus(mapWidth);
        bonus.setState(type);
        bonusList.add(bonus);
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

