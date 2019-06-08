package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;

public class Signal {
    private Vector2 position;
    public Vector2 dimension;
    private TextureRegion Region;
    private int team;
    public Signal(){
        init();
    }

    private void init(){
        team = 0;
        dimension = new Vector2(0.2f,0.2f);
        position = new Vector2(0,0);
    }

    public void renderTeamSignal(SpriteBatch batch){
        if(team == Constants.PLAYER.RED_TEAM){
            Region = AssetsController.instance.getRegion("redTeamSignal");
            batch.draw(Region,position.x,position.y,dimension.x,dimension.y);
        }
        else if(team == Constants.PLAYER.BLUE_TEAM){
            Region = AssetsController.instance.getRegion("blueTeamSignal");
            batch.draw(Region,position.x,position.y,dimension.x,dimension.y);
        }
    }

    public void renderCurPlayerSignal(SpriteBatch batch){
        dimension = new Vector2(0.35f,0.35f);
        Region = AssetsController.instance.getRegion("curPlayerSignal");
        batch.draw(Region,position.x,position.y,dimension.x,dimension.y);
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

    public void setTeam(int team){
        this.team = team;
    }

}
