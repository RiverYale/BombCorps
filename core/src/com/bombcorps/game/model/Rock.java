package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;

public class Rock {
    public final float width = Gdx.graphics.getWidth();
    public final float height = Gdx.graphics.getHeight();
    public Vector2 position;
    public Vector2 dimension;
    public Rectangle rectangle;

    private TextureRegion region;
    private int length;
    public Rock(){
        init();
    }

    private void init(){
        region = AssetsController.instance.getRegion("rock");
        dimension = new Vector2(width/Constants.VIEWPORT_WIDTH,height/Constants.VIEWPORT_HEIGHT);
        position = new Vector2(0,0);
        rectangle = new Rectangle(0,0,dimension.x,dimension.y);
        setLength(1);
    }



    public void setLength(int length) {
        this.length = length;
    }

    public void increaseLength(int amount){
        setLength(length+amount);
    }

    public void render(SpriteBatch batch){
        batch.draw(region,position.x,position.y,dimension.x,dimension.y);
    }

    public Vector2 getPosition(){
        return position;
    }

    public Rectangle getRect(){
        return rectangle;
    }

}