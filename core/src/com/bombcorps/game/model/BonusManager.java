package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.model.bombs.Bomb;

public class BonusManager {
    private Array<Bonus> bonusList;
    private int mapWidth;

    public BonusManager(int mapWidth){
        bonusList = new Array<Bonus>();
        this.mapWidth = mapWidth;
    }

//    public void addBonus(int type){             //输入bonus的类别，产生该种bonus,位置随机
//
//        Bonus bonus = new Bonus();
//        bonus.setState(type);
//        bonusList.add(bonus);
//    }

    public void update(float deltaTime){
        for(int i = 0 ; i < bonusList.size ; i++){
            if(bonusList.get(i).attachTo){
                bonusList.removeIndex(i);
            }
        }
        for(int i = 0 ; i < bonusList.size ; i++){
            bonusList.get(i).update(deltaTime);
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

