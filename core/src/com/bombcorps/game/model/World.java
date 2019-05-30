package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.logging.Level;

public class World {
    public static final String TAG = Level.class.getName();
    private int levelTurn=0;
    private int Turn=0;

    public Array<Player> getPlayers() {
        return playerManager.getPlayerList();
    }

    public String getIp() {
        return playerManager.getPlayerList().get(0).getIp();
    }

    public void removePlayer(Player p) {
        int pIndex;
        for(pIndex = 0; pIndex < playerManager.getPlayerList().size; pIndex++){
            if(p.equals(playerManager.getPlayerList().get(pIndex))){
                break;
            }
        }
        playerManager.deletePlayerAtIndex(pIndex);
    }

    public void addBonus(Bonus b) {
        bonusManager.setBonusByChance();
    }

    public Player getNextPlayer() {
        Turn++;
        if(Turn>=playerManager.getPlayerList().size){
            Turn -= playerManager.getPlayerList().size;
        }
        return playerManager.getPlayerList().get(Turn);
    }

    public Bonus spawnBonus() {
        return bonusManager.getBonusList().pop();
    }

    public Player getFirstPlayer() {
        Turn++;
        return playerManager.getPlayerList().first();
    }

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
    private Array<Rock> rocks;
    //英雄
    private PlayerManager playerManager;
    //
    private BonusManager bonusManager;
    //地图宽度
    private int MapWidth;

    public  World(Room room){
        this.playerManager = room.getPlayerManager();
        init(room.getMapName());
    }

    private void init(String filename){
        //物品
        rocks = new Array<Rock>();
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        MapWidth = pixmap.getWidth();
        bonusManager = new BonusManager(MapWidth);
        //从左上到右下扫描
        int lastPixel = -1;
        for(int pixelY = 0; pixelY < pixmap.getHeight();pixelY++)
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                float offsetHeight = 0;
                float baseHeight = pixmap.getHeight() - pixelY;
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                //空地
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {

                }
                //障碍物
                else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
                    if (lastPixel != currentPixel) {
                        Rock rock = new Rock();
                        rock.position.set(pixelX, baseHeight * rock.dimension.y + offsetHeight);
                        rocks.add(rock);
                    } else {
                        rocks.get(rocks.size - 1).increaseLength(1);
                    }
                }
                //英雄出生点
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                    Vector2 position = new Vector2(pixelX, baseHeight + offsetHeight);
                    playerManager.getPlayerList().get(levelTurn).getMyHero().setPosition(position);
                    levelTurn++;
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
        pixmap.dispose();
        Gdx.app.debug(TAG,"World"+filename+"loaded");
    }

    public void render(SpriteBatch batch){
        for (Rock rock : rocks){
            rock.render(batch);
        }
        playerManager.render(batch);
        bonusManager.render(batch);
    }

    public int getMapWidth() {
        return MapWidth;
    }
}
