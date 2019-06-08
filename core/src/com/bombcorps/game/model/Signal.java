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
    private Player.TEAM team;
    public Signal(){
        init();
    }

    private void init(){
        team = Player.TEAM.BLUE;
        dimension = new Vector2(0.2f,0.2f);
        position = new Vector2(0,0);
    }

    public void renderTeamSignal(SpriteBatch batch){
        if(team == Player.TEAM.RED){
            Region = AssetsController.instance.getRegion("redTeamSignal");
            batch.draw(Region,position.x,position.y,dimension.x,dimension.y);
        }
        else if(team == Player.TEAM.BLUE){
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
        this.position.set(v,v1);
    }

    public void setPositionX(float x){
        position.x = x;
    }

    public void setPositionY(float y){
        position.y = y;
    }

    public void setTeam(Player.TEAM team){
        this.team = team;
    }

}
