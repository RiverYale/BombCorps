package com.bombcorps.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bombcorps.game.controller.AssetsController;

public class Rock {
    public Vector2 position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;

    private TextureRegion regionLeft;
    private TextureRegion regionRight;
    private TextureRegion regionMiddle;
    private int length;
    public Rock(){
        init();
    }

    private void init(){
        dimension.set(1,1.5f);
        regionLeft = AssetsController.instance.getRegion("rock.left");
        regionRight = AssetsController.instance.getRegion("rock.right");
        regionMiddle = AssetsController.instance.getRegion("rock.middle");
        setLength(1);
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void increaseLength(int amount){
        setLength(length+amount);
    }

    public void render(SpriteBatch batch){
        TextureRegion region = null;
        float relX = 0;
        float relY = 0;
        //画左边砖块
        region = regionLeft;
        relX -= dimension.x/4;
        batch.draw(region.getTexture(),position.x+relX,position.y+relY,origin.x,origin.y,dimension.x,
                dimension.y,scale.x,scale.y,rotation,region.getRegionX(),region.getRegionY(),
                region.getRegionWidth(),region.getRegionHeight(),false,false);

        //画中间砖块
        relX = 0;
        region = regionMiddle;
        for (int i = 0; i < length; i++){
            batch.draw(region.getTexture(),position.x+relX,position.y+relY,origin.x,origin.y,dimension.x,
                    dimension.y,scale.x,scale.y,rotation,region.getRegionX(),region.getRegionY(),
                    region.getRegionWidth(),region.getRegionHeight(),false,false);
            relX += dimension.x;
        }

        //画右边方块
        region = regionRight;
        batch.draw(region.getTexture(),position.x+relX,position.y+relY,origin.x,origin.y,dimension.x,
                dimension.y,scale.x,scale.y,rotation,region.getRegionX(),region.getRegionY(),
                region.getRegionWidth(),region.getRegionHeight(),false,false);
    }
}