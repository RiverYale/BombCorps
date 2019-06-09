package com.bombcorps.game.model;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;

import java.io.Serializable;

public class Bonus implements Serializable {
    private Vector2 position;
    private Vector2 origin;
    private Vector2 scale;
    private Vector2 dimension;
    public TextureRegion bonusBox;
    public TextureRegion parachute;
    public boolean attachTo = false;

    private Rectangle rec;

    private TYPE type;
    public enum TYPE{
        ADDHEALTH, ADDENDURANCE
    }

    private STATE state;
    public enum STATE{
        ACTIVATED, GROUNDED
    }

    public Bonus(Vector2 position, int type){
        init();
        this.position = position;
        setType(type);
//        initType();
    }

    public Bonus(Message.MBonus m) {
        position = m.position;
        origin = m.origin;
        scale = m.scale;
        dimension = m.dimension;
        type = m.type;
        state = m.state;
        bonusBox = AssetsController.instance.getRegion("BonusBox");
        parachute = AssetsController.instance.getRegion("Parachute");

        rec = new Rectangle(0,0,dimension.x, dimension.y);
    }

    public Message.MBonus getMBonus() {
        return new Message.MBonus(position, origin, scale, dimension, type, state);
    }

    public void init(){
        state = STATE.ACTIVATED;
        scale = new Vector2(1,1);
        dimension = Constants.BONUS.DIMENSION;
        origin = new Vector2(dimension.x / 2, dimension.y / 2);
        bonusBox = AssetsController.instance.getRegion("BonusBox");
        parachute = AssetsController.instance.getRegion("Parachute");
        rec = new Rectangle(0,0,dimension.x, dimension.y);
//        initPosition();
    }

//    public void initPosition(float mapWidth){
//        position = new Vector2();
//        position.x = (float)Math.random() * (mapWidth - dimension.x);
//    }

    public void attachTo(Player player){
        attachTo = true;
        switch (type){
            case ADDENDURANCE:
                player.getMyHero().setEndurance(MathUtils.clamp(
                        player.getMyHero().getEndurance() + Constants.BONUS.ENDURENCE,
                        0, Constants.MAX_ENDURENCE));
                break;
            case ADDHEALTH:
                player.getMyHero().setHealth(MathUtils.clamp(
                        player.getMyHero().getHealth() + Constants.BONUS.HEALTH,0,
                        player.getMyHero().getMaxHealth() ));
                break;
        }
    }

    public void update(float deltaTime){
        switch (state){
            case ACTIVATED:
                position.y += deltaTime * Constants.BONUS.BONUS_VELOCITY_Y;
                break;
        }
    }

    public void render(SpriteBatch batch){
        if(state == STATE.ACTIVATED);
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

    public void setType(int type){
        if(type == Constants.BONUS.ADDHEALTH){
            this.type = TYPE.ADDHEALTH;
        }else{
            this.type = TYPE.ADDENDURANCE;
        }
    }

    public Rectangle getRect(){
        rec.x = position.x;
        rec.y = position.y;
        return rec;
    }

}