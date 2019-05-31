package com.bombcorps.game.model;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;

public class Bonus {
    private Vector2 position;
    private Vector2 origin;
    private Vector2 scale;
    private Vector2 dimension;
    public TextureRegion bonusBox;
    public TextureRegion parachute;

    private TYPE type;
    private enum TYPE{
        ADDHEALTH, ADDENDURANCE
    }

    private STATE state;
    private enum STATE{
        ACTIVATED, GROUNDED
    }

    public Bonus(int mapWidth){
        init(mapWidth);
        initType();
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

    public void initType(){
        if(Math.random() > 0.5)
            this.type = TYPE.ADDENDURANCE;
        else
            this.type = TYPE.ADDHEALTH;
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
            batch.draw(parachute, position.x, position.y + dimension.y,
                    origin.x, origin.y, dimension.x, dimension.y,
                    scale.x, scale.y,0);


        batch.draw(bonusBox, position.x, position.y,
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