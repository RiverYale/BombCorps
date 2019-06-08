package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;

public class CurPlayerSignal {
    private Vector2 position;
    public Vector2 dimension;
    private TextureRegion region;
    public CurPlayerSignal(){
        init();
    }

    private void init(){
        region = new TextureRegion(new Texture(Gdx.files.internal("gamescreen/curPlayerSignal.png")));
        dimension = new Vector2(0.2f,0.2f);
        position = new Vector2(0,0);
    }

    public void render(SpriteBatch batch){
        batch.draw(region,position.x,position.y,dimension.x,dimension.y);
    }

    public void setPosition(Vector2 position){
        this.position = position;
    }

    public Vector2 getPosition(){
        return position;
    }

    public void setPosition(float v, float v1) {
        this.position.x = v;
        this.position.y = v1;
    }
}
