package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.logging.Level;

public class World {
    public static final String TAG = Level.class.getName();
    private final float width = Gdx.graphics.getWidth();
    private final float height = Gdx.graphics.getHeight();
    private int levelTurn=0;
    private int Turn=0;

    public Array<Player> getPlayers() {
        return playerManager.getAllPlayerList();
    }

    public String getIp() {
        return playerManager.getAllPlayerList().get(0).getIp();
    }

    public void removePlayer(Player p) {
        int pIndex;
        for(pIndex = 0; pIndex < playerManager.getAllPlayerList().size; pIndex++){
            if(p.equals(playerManager.getAllPlayerList().get(pIndex))){
                break;
            }
        }
        playerManager.deletePlayerAtIndex(pIndex,p.getTeam());
    }

    public void addBonus(Bonus b) {

        bonusManager.getBonusList().add(b);
    }

    public Player getNextPlayer() {
        Turn++;
        if(Turn>=playerManager.getAllPlayerList().size){
            Turn -= playerManager.getAllPlayerList().size;
        }
        return playerManager.getAllPlayerList().get(Turn);
    }

    public Bonus spawnBonus() {
        return bonusManager.getBonusList().pop();
    }

    public Player getFirstPlayer() {
        Turn++;
        return playerManager.getAllPlayerList().first();
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
    public Array<Rock> rocks;
    //英雄
    private PlayerManager playerManager;
    //
    public BonusManager bonusManager;
    //地图宽度
    private float MapWidth;

    public  World(Room room){
        this.playerManager = room.getPlayerManager();
        init(room.getMapName());
    }

    private void init(String filename){
        //物品
        rocks = new Array<Rock>();
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        MapWidth = pixmap.getWidth()*width/Constants.VIEWPORT_WIDTH;
        bonusManager = new BonusManager(MapWidth);
        //从左上到右下扫描
        for(int pixelY = 0; pixelY < pixmap.getHeight();pixelY++)
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                float baseHeight = pixmap.getHeight() - pixelY;
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                //空地
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel)) {

                }
                //障碍物
                else if (BLOCK_TYPE.ROCK.sameColor(currentPixel)) {
                    Rock rock = new Rock();
                    rock.position.set(pixelX*rock.dimension.x, baseHeight*rock.dimension.y);
                    rock.rectangle.setPosition(rock.position);
                    rocks.add(rock);
                }
                //英雄出生点
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                    Vector2 position = new Vector2(pixelX*width/Constants.VIEWPORT_WIDTH,baseHeight*height/Constants.VIEWPORT_HEIGHT );
                    playerManager.getAllPlayerList().get(levelTurn).getMyHero().setPosition(position);
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
            }
        pixmap.dispose();
        Gdx.app.debug(TAG,"World"+filename+"loaded");
    }

    public void render(SpriteBatch batch){
        batch.draw(new Texture(Gdx.files.internal("maps/background.png")),0,0,Gdx.graphics.getWidth()/Constants.VIEWPORT_WIDTH*30,Gdx.graphics.getHeight()/Constants.VIEWPORT_HEIGHT*20);
        for (Rock rock : rocks){
            rock.render(batch);
        }
        playerManager.render(batch);
        bonusManager.render(batch);
    }

    public float getMapWidth() {
        return MapWidth;
    }
}
