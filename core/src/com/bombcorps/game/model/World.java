package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.model.heros.AbstractHero;

import java.util.logging.Level;

public class World {
    public static final String TAG = Level.class.getName();

    public enum BLOCK_TYPE{
        EMPTY(0,0,255),//空地，蓝
        ROCK(0,255,0),//砖块，绿
        PLAYER_SPAWNPOINT(255,255,255);//英雄出生点，白
        private int color;

        BLOCK_TYPE(int r, int g, int b){
            color = (r << 24) | (g << 16) | (b << 8) | 0xff;
        }

        public boolean sameColor(int color){
            return this.color == color;
        }

        public int getColor(){
            return color;
        }

    }

    //砖块
    public Array<Rock> rocks;
    //英雄
    public Array<AbstractHero> heroes;

    private int MapWidth;

    public  World(String filename){
        init(filename);
    }

    private void init(String filename){
        //物品
        rocks = new Array<Rock>();
        //英雄
        heroes = new Array<AbstractHero>();
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        MapWidth = pixmap.getWidth();
        //从左上到右下扫描
        int lastPixel = -1;
        for(int pixelY = 0; pixelY < pixmap.getHeight();pixelY++){
            for(int pixelX = 0; pixelX<pixmap.getWidth();pixelX++){
                float offsetHeight = 0;
                float baseHeight = pixmap.getHeight() - pixelY;
                int currentPixel = pixmap.getPixel(pixelX,pixelY);
                //空地
                if(BLOCK_TYPE.EMPTY.sameColor(currentPixel)){

                }
                //障碍物
                else if(BLOCK_TYPE.ROCK.sameColor(currentPixel)){
                    if(lastPixel!=currentPixel){
                        Rock rock = new Rock();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        rock.position.set(pixelX,baseHeight*rock.dimension.y*heightIncreaseFactor+offsetHeight);
                        rocks.add(rock);
                    }
                    else {
                        rocks.get(rocks.size-1).increaseLength(1);
                    }
                }
                //英雄出生点
                else if(BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)){

                }
                //未知错误
                else {
                    int r = 0xff & (currentPixel >>> 24); // red color channel
                    int g = 0xff & (currentPixel >>> 16); // green color channel
                    int b = 0xff & (currentPixel >>> 8); // blue color channel
                    int a = 0xff & currentPixel; // alpha channel
                    Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<"
                            + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b
                            + "> a<" + a + ">");
                }
                lastPixel = currentPixel;
            }


        }
        pixmap.dispose();
        Gdx.app.debug(TAG,"World"+filename+"loaded");
    }

    public void render(SpriteBatch batch){
        for (Rock rock : rocks){
            rock.render(batch);
        }
    }

    public int getMapWidth() {
        return MapWidth;
    }
}
