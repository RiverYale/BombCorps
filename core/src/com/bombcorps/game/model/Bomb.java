package com.bombcorps.game.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Bomb {
    private TextureRegion bomb;
    private float routeScale;

    private Vector2 position;
    private Vector2 dimension;
    private Vector2 bombScale;
    private Vector2 origin;
    private Vector2 velocity;
    private float rotation;

    public enum STATE{
        FLY, BOOM
    }

    public Bomb(float angle, float speed){

    }

    public void init(){

    }

    public void update(float deltaTime){

    }

    public void updateVelocity(float deltaTime){

    }

    public void updatePosition(float deltaTime){

    }

    public void render(SpriteBatch batch){

    }

    public void renderBomb(SpriteBatch batch){

    }

    public void renderBoom(SpriteBatch batch){

    }

}
