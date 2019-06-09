package com.bombcorps.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bombcorps.game.controller.AssetsController;

import java.util.logging.Level;

public class World {
    public static final String TAG = Level.class.getName();
    private int levelTurnBlue = 0;
    private int levelTurnRed = 0;
    private int team = Constants.PLAYER.RED_TEAM;
    private Player nextRedPlayer;
    private Player nextBluePlayer;

    public Array<Player> getPlayers() {
        return playerManager.getAllPlayerList();
    }

    public String getIp() {
        return hostIP;
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
        Player player;
        if(team == Constants.PLAYER.RED_TEAM){
            team = Constants.PLAYER.BLUE_TEAM;
            player = nextRedPlayer;
            int ir;
            for(ir = 0; ir< playerManager.getRedPlayerList().size; ir++){
                if(nextRedPlayer.equals(playerManager.getRedPlayerList().get(ir))){
                    nextRedPlayer = playerManager.getRedPlayerList().get((ir+1)%playerManager.getRedPlayerList().size);
                    break;
                }
            }
        }
        else{
            team = Constants.PLAYER.RED_TEAM;
            player = nextBluePlayer;
            int ir;
            for(ir = 0; ir< playerManager.getBluePlayerList().size; ir++){
                if(nextBluePlayer.equals(playerManager.getBluePlayerList().get(ir))){
                    nextBluePlayer = playerManager.getBluePlayerList().get((ir+1)%playerManager.getBluePlayerList().size);
                    break;
                }
            }
        }
        return player;
    }

    public Bonus spawnBonus() {
        if(bonusManager.getBonusList().size > 0)
            return bonusManager.getBonusList().get(bonusManager.getBonusList().size - 1);

        return null;
    }

    public Bonus getFallingBonus() {
        if(bonusManager.getBonusList().size > 0)
            return bonusManager.getBonusList().get(bonusManager.getBonusList().size - 1);

        return null;
    }

    public Player getFirstPlayer() {
        team = Constants.PLAYER.BLUE_TEAM;
        Player p = nextRedPlayer;
        int ir;
        for(ir = 0; ir< playerManager.getRedPlayerList().size; ir++){
            if(nextRedPlayer.equals(playerManager.getRedPlayerList().get(ir))){
                nextRedPlayer = playerManager.getRedPlayerList().get((ir+1)%playerManager.getRedPlayerList().size);
                break;
            }
        }
        return p;
    }

    public enum BLOCK_TYPE{
        EMPTY(0,0,255),//空地，蓝
        ROCK(0,255,0),//砖块，绿
        PLAYER_SPAWNPOINT(255,255,255),//英雄出生点，白
        PILLAR(255,0,0);//柱子，红
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
    //柱子
    public Array<Pillar> pillars;
    //标志
    //英雄
    private PlayerManager playerManager;
    //
    public BonusManager bonusManager;
    //地图宽度
    private float MapWidth;

    private String hostIP;

    private int limit;


    public  World(Room room){
        hostIP = room.getOwnerIp();
        limit = room.getLIMIT();
        playerManager = room.getPlayerManager();
        init(room.getMapName());
    }

    private void init(String filename){
        nextRedPlayer = playerManager.getRedPlayerList().first();
        nextBluePlayer = playerManager.getBluePlayerList().first();
        //物品
        rocks = new Array<Rock>();
        pillars = new Array<Pillar>();
        Pixmap pixmap = new Pixmap(Gdx.files.internal("map/map"+filename+".png"));
        MapWidth = 32;
        bonusManager = new BonusManager((int)MapWidth);
        //从左上到右下扫描
        for(int pixelY = 0; pixelY < pixmap.getHeight();pixelY++)
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++) {
                float baseHeight = pixmap.getHeight() - pixelY-1;
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
                //柱子
                else if(BLOCK_TYPE.PILLAR.sameColor(currentPixel)){
                    Pillar pillar = new Pillar();
                    Vector2 position = new Vector2(pixelX*pillar.dimension.x, baseHeight*pillar.dimension.y);
                    pillar.setPosition(position);
                    pillar.setPosition(position);
                    if(baseHeight == 0){
                        pillar.setState(Pillar.State.BASE);
                        pillar.getRect().setPosition(pillar.getPosition());
                        pillars.add(pillar);
                        break;
                    }
                    int BPixel = pixmap.getPixel(pixelX,pixelY+1);
                    if(!BLOCK_TYPE.PILLAR.sameColor(BPixel)){
                        pillar.setState(Pillar.State.BASE);
                    }else {
                        pillar.setState(Pillar.State.MIDDLE);
                    }
                    pillar.getRect().setPosition(pillar.getPosition());
                    pillars.add(pillar);
                }
                //英雄出生点
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)) {
                    Vector2 position = new Vector2(pixelX,baseHeight);
                    if(levelTurnRed <playerManager.getRedPlayerList().size){
                        playerManager.getRedPlayerList().get(levelTurnRed).getMyHero().setPosition(position);
                        levelTurnRed++;
                    }else if(levelTurnBlue<playerManager.getBluePlayerList().size){
                        playerManager.getBluePlayerList().get(levelTurnBlue).getMyHero().setPosition(position);
                        levelTurnBlue++;
                    }
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
        Gdx.app.log(TAG,"World"+filename+"loaded");
    }

    public void render(SpriteBatch batch){
       batch.draw(AssetsController.instance.getRegion("gamebackground"),-16,0,64,30);
       for (Rock rock : rocks){
           rock.render(batch);
       }
       for(Pillar pillar : pillars){
           pillar.render(batch);
       }
        bonusManager.render(batch);
        playerManager.render(batch);
        playerManager.getBomb().render(batch);
    }

    public float getMapWidth() {
        return MapWidth;
    }

    public PlayerManager getPlayerManager(){
        return playerManager;
    }

    public int getAlivePlayerNum(){
        int Num = 0;
        for(Player p : playerManager.getAllPlayerList()){
            if(p.getMyHero().getState()!=Constants.STATE_DEAD){
                Num++;
            }
        }
        return Num;
    }

    public int getLimit(){
        return  limit;
    }

}
