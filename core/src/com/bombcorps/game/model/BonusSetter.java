package com.bombcorps.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.controller.AssetsController;

public class BonusSetter{
    private Array<Bonus> bonusList;
    private int mapWidth;

    public BonusSetter(int mapWidth){
        bonusList = new Array<Bonus>();
        this.mapWidth = mapWidth;
    }

    public void setBonusByChance(){
        if(Math.random() > Constants.BONUS.BONUS_CHANCE)
            return;

        bonusList.add(new Bonus(mapWidth));
    }

    public void update(float deltaTime){
        for(int i = 0 ; i < bonusList.size; i++){
            bonusList.get(i).update(deltaTime);
        }
    }

    public void render(SpriteBatch batch){
        for(int i = 0 ; i < bonusList.size; i++){
            bonusList.get(i).render(batch);
        }
    }

    public Array<Bonus> getBonusList(){
        return bonusList;
    }

    public void deleteIndexAt(int index){
        bonusList.removeIndex(index);
    }

}

class Bonus {
    private Vector2 position;
    private Vector2 origin;
    private Vector2 scale;
    private Vector2 dimension;
    public TextureRegion bonusBox;
    public TextureRegion parachute;

    private STATE state;
    public enum STATE{
         ACTIVATED, GROUNDED
    }

    public Bonus(int mapWidth){
        init(mapWidth);
    }

    public void init(int mapWidth){
        state = STATE.ACTIVATED;
        origin = new Vector2();
        scale = new Vector2();
        dimension = Constants.BONUS.DIMENSION;
        bonusBox = AssetsController.instance.getRegion("BonusBox");
        parachute = AssetsController.instance.getRegion("Parachute");

        initPosition(mapWidth);
    }

    public void initPosition(int mapWidth){
        position = new Vector2();
        position.x = (float)Math.random() * (mapWidth - dimension.x);
    }

    public void update(float deltaTime){
        switch (state){
            case ACTIVATED:
                position.y += deltaTime * Constants.BONUS.BONUS_VELOCITY_Y;
                break;
        }
    }

    public void render(SpriteBatch batch){
        if(state == STATE.ACTIVATED)
            batch.draw(bonusBox, position.x, position.y,
                origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y,0);

        batch.draw(parachute, position.x, position.y + dimension.y,
                origin.x, origin.y, dimension.x, dimension.y,
                scale.x, scale.y,0);
    }

    public void setState(int input){
        switch (input){
            case Constants.BONUS.GROUNDED:
                state = STATE.GROUNDED;
                break;
            case Constants.BONUS.ACTIVATED:
                state = STATE.ACTIVATED;
                break;
        }
    }

}
