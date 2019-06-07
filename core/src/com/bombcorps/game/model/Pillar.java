package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.view.AbstractGameScreen;


public class Pillar {
    public final float width = Gdx.graphics.getWidth();
    public final float height = Gdx.graphics.getHeight();
    private Vector2 position;
    public Vector2 dimension;
    private Rectangle rectangle;
    private Vector2 origin;
    private Vector2 scale;
    private float rotation;
    State state;

    public enum State{
        MIDDLE,BASE
    }


    private TextureRegion pillarMiddle;
    private TextureRegion pillarBase;
    private int length;
    public Pillar(){
        init();
    }

    private void init(){
        pillarBase = AssetsController.instance.getRegion("pillarBase");
        pillarMiddle = AssetsController.instance.getRegion("pillarMiddle");
        dimension = new Vector2(width/32,height/20);
        rectangle = new Rectangle(0,0,dimension.x,dimension.y);
        position = new Vector2(0,0);

        state = State.MIDDLE;
        setLength(1);
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void increaseLength(int amount){
        setLength(length+amount);
    }

    public void render(SpriteBatch batch){
        if(state == State.MIDDLE){
            batch.draw(pillarMiddle,position.x+4*dimension.x/32,position.y,dimension.x*24/32,dimension.y);
        }else if(state == State.BASE){
            batch.draw(pillarBase,position.x,position.y,dimension.x,dimension.y);
        }
    }

    public Rectangle getRect() {
        return rectangle;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setPosition(Vector2 position){
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

}
