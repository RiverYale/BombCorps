package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.AudioController;
import com.bombcorps.game.controller.DataController;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.Room;


public class RoomScreen extends AbstractGameScreen implements InputProcessor{
    private static final String TAG = RoomScreen.class.getName();
    private static final float height = Gdx.graphics.getHeight();
    private static final float width = Gdx.graphics.getWidth();
    //初值
    private DirectedGame game;
    private Room room;
    private Player myplayer;
    private int mode;
    private String ip;
    DataController dc = DataController.instance;
    String localHostIp;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    //背景
    private TextureRegion roomBackground;   //房间背景
    private Sprite selectBackground;    //选择背景
    private Sprite doorRed;
    private Sprite doorBlue;
    private Sprite siteRed[];
    private Sprite siteBlue[];
    //按钮
    private Sprite btnMapLeft;
    private Sprite btnMapRight;
    private Sprite btnHeroLeft;
    private Sprite btnHeroRight;
    private Sprite btnReady;
    private Sprite btnCancel;
    private Sprite btnToLobby;
    //字体
    private BitmapFont font;

    private int heroSelect = Constants.SPARDA;
    private int mapNum = 0;
    private boolean b_error = false;
    public static boolean b_loadGame = false;

    private int level[];

    public RoomScreen(DirectedGame game,String ip,int mode,Room room) {
        super(game);
        this.game = game;
        this.ip = ip;
        this.mode = mode;
        this.room = room;
        localHostIp = NetController.getLocalHostIp();

        batch = new SpriteBatch();
        camera = new OrthographicCamera(width, height);
        camera.position.set(width/2, height/2, 0);
        camera.update();
        //默认地图
        room.setMapName("0");
        //添加新玩家
        myplayer = new Player(dc.getName());
        myplayer.setIp(localHostIp);
        room.addPlayer(myplayer);
        //本地玩家指向房间内玩家
        for(int i = 0;i < room.getPlayerManager().getAllPlayerList().size;i ++){
            if(localHostIp.equals(room.getPlayerManager().getAllPlayerList().get(i).getIp())){
                myplayer = room.getPlayerManager().getAllPlayerList().get(i);
                break;
            }
        }
        //设置默认英雄和等级
        myplayer.setLevel(dc.getPersonalData(DataController.SPARDAR));
        //非房主玩家向其他玩家发送信号
        if(!localHostIp.equals(this.ip)){
            //Gdx.app.log("I am in","this room");
            game.getNetController().enterRoom(this.ip,myplayer);
        }

        init();
    }

    public void init(){
        font = AssetsController.instance.font;
        //房间背景
        roomBackground = new TextureRegion(AssetsController.instance.getRegion("roombackground"));
        //选择背景
        selectBackground = new Sprite(AssetsController.instance.getRegion("selectbackground"));
        selectBackground.setSize(0.26667f * width,0.71f * height);
        selectBackground.setPosition(0.0444f * width,0.13f * height);
        //红蓝门
        doorRed = new Sprite(AssetsController.instance.getRegion("reddoor"));
        doorRed.setSize(0.2667f * width, 0.2f * height);
        doorRed.setPosition(0.37f * width, 0.73f * height);
        doorBlue = new Sprite(AssetsController.instance.getRegion("bluedoor"));
        doorBlue.setSize(0.2667f * width, 0.2f * height);
        doorBlue.setPosition(doorRed.getX() + 0.3f * width, 0.73f * height);
        //红蓝方位置
        float siteheight = 0.3f * height;
        float sitewidth = doorRed.getWidth() / 2;
        siteRed = new Sprite[4];
        siteBlue = new Sprite[4];
        for (int i = 0; i < 4; i++) {
            siteRed[i] = new Sprite(AssetsController.instance.getRegion("space"));
            siteBlue[i] = new Sprite(AssetsController.instance.getRegion("space"));
            siteRed[i].setSize(sitewidth, siteheight);
            siteBlue[i].setSize(sitewidth, siteheight);
            if (i == 0) {
                siteRed[i].setPosition(doorRed.getX(), doorRed.getY() - siteRed[i].getHeight());
                siteBlue[i].setPosition(doorBlue.getX(), doorBlue.getY() - siteBlue[i].getHeight());
            }
            if (i == 1) {
                siteRed[i].setPosition(siteRed[0].getX() + siteRed[0].getWidth(), siteRed[0].getY());
                siteBlue[i].setPosition(siteBlue[0].getX() + siteBlue[0].getWidth(), siteBlue[0].getY());
            }
            if (i == 2) {
                siteRed[i].setPosition(siteRed[0].getX(), siteRed[0].getY() - siteRed[0].getHeight());
                siteBlue[i].setPosition(siteBlue[0].getX(), siteBlue[0].getY() - siteBlue[0].getHeight());
            }
            if (i == 3) {
                siteRed[i].setPosition(siteRed[2].getX() + siteRed[2].getWidth(), siteRed[2].getY());
                siteBlue[i].setPosition(siteBlue[2].getX() + siteBlue[2].getWidth(), siteBlue[2].getY());
            }
        }
        //返回按钮
        btnToLobby = new Sprite(AssetsController.instance.getRegion("mapleft"));
        btnToLobby.setSize(0.045f * width,0.07f * height);
        btnToLobby.setPosition(0.0222f * width,0.91f * height);
        //开始/取消/开始游戏按钮
        btnReady = new Sprite(AssetsController.instance.getRegion("ready"));
        btnReady.setSize(0.07778f * width,0.06f*height);
        btnReady.setPosition(selectBackground.getX() + selectBackground.getWidth()/2-btnReady.getWidth()/2-0.06f*width,
                0.16f*height);
        btnCancel = new Sprite(AssetsController.instance.getRegion("cancel"));
        btnCancel.setSize(0.07778f * width,0.06f*height);
        btnCancel.setPosition(selectBackground.getX() + selectBackground.getWidth()/2-btnCancel.getWidth()/2+0.06f*width,
                0.16f*height);
        //地图选择
        btnMapLeft = new Sprite(AssetsController.instance.getRegion("mapleft"));
        btnMapLeft.setSize(0.06667f * width,0.08f * height);
        btnMapLeft.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnMapLeft.getWidth()/2- 0.08f*width,
                0.4f * height);
        btnMapRight = new Sprite(AssetsController.instance.getRegion("mapright"));
        btnMapRight.setSize(0.06667f * width,0.08f * height);
        btnMapRight.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnMapRight.getWidth()/2 + 0.08f * width,
                0.4f * height);
        //英雄选择按钮
        btnHeroLeft = new Sprite(AssetsController.instance.getRegion("heroleft"));
        btnHeroLeft.setSize(0.06667f * width,0.06f * height);
        btnHeroLeft.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnHeroLeft.getWidth()/2- 0.08f*width,
                0.28f * height);
        btnHeroRight = new Sprite(AssetsController.instance.getRegion("heroright"));
        btnHeroRight.setSize(0.06667f * width,0.06f * height);
        btnHeroRight.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnHeroRight.getWidth()/2 + 0.08f * width,
                0.28f * height);
        //英雄尺寸保存
        heroWidth = new float[5];
        heroHeight = new float[5];
        for(int i = 0;i < 5;i ++){
            heroWidth[i] = hero[i].getWidth();
            heroHeight[i] = hero[i].getHeight();
        }
        //英雄等级
        level= new int[5];
        level[Constants.SPARDA] = DataController.SPARDAR;
        level[Constants.WIZARD] = DataController.WIZARD;
        level[Constants.SNIPER] = DataController.SNIPER;
        level[Constants.PROTECTOR] = DataController.PROTECTOR;
        level[Constants.ANGEL] = DataController.ANGEL;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawBackground();
        drawButton();
        drawHero();
        drawMap();
        drawSite();
        drawPersonNum();
        if(b_error){
            //Gdx.app.log("zc", "owner quit");
            drawError();
        }
        if (b_loadGame) {
            game.loadGameScreen();
        }

        batch.end();
    }

    public void drawBackground(){
        batch.draw(roomBackground,0,0,width,height);
        selectBackground.draw(batch);
        doorRed.draw(batch);
        doorBlue.draw(batch);
        for(int i = 0;i < 4;i ++){
            siteRed[i].draw(batch);
            siteBlue[i].draw(batch);
        }
        //准备人数
    }

    public void drawButton(){
        btnToLobby.draw(batch);
        btnHeroLeft.draw(batch);
        btnHeroRight.draw(batch);
        if(localHostIp.equals(room.getOwnerIp())){
            btnMapLeft.draw(batch);
            btnMapRight.draw(batch);
        }
        int readyNum = 0;
        for(int i = 0;i < room.getPlayerManager().getAllPlayerList().size;i ++){
            if(room.getPlayerManager().getAllPlayerList().get(i).getReady()){
                readyNum ++;
            }
        }
        //准备/取消按钮布置
        if(myplayer.getIp().equals(room.getOwnerIp()) && (readyNum == (2 * mode - 1))){
            btnReady.draw(batch);
        }

        if(!myplayer.getIp().equals(room.getOwnerIp()) && myplayer.getReady()){
            btnCancel.draw(batch);
        }

        if(!myplayer.getIp().equals(room.getOwnerIp())&& !myplayer.getReady()){
            btnReady.draw(batch);
        }
    }

    private Sprite hero[] = {
            new Sprite(AssetsController.instance.getRegion("Sparda_stand")),
            new Sprite(AssetsController.instance.getRegion("Protector_stand")),
            new Sprite(AssetsController.instance.getRegion("Angel_stand")),
            new Sprite(AssetsController.instance.getRegion("Sniper_stand")),
            new Sprite(AssetsController.instance.getRegion("Wizard_stand"))
    };

    private float heroWidth[];
    private float heroHeight[];
    public void drawHero(){
        hero[heroSelect].setSize((heroWidth[heroSelect]/900)*width,(heroHeight[heroSelect]/500)*height);
        hero[heroSelect].setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - hero[heroSelect].getWidth()/2,
                btnHeroLeft.getY());
        hero[heroSelect].draw(batch);
    }

    private Sprite map[] = {
            new Sprite(AssetsController.instance.getRegion("scalemap0")),
            new Sprite(AssetsController.instance.getRegion("scalemap1"))
    };

    private void drawMap(){
        mapNum = Integer.parseInt(room.getMapName());
        map[mapNum].setSize(0.23f * width,0.3f * height);
        map[mapNum].setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - map[mapNum].getWidth()/2,
                0.51f * height);
        map[mapNum].draw(batch);
    }

    private Sprite redTeamHero[][] = {
            {
                    new Sprite(AssetsController.instance.getRegion("Sparda_stand")),
                    new Sprite(AssetsController.instance.getRegion("Protector_stand")),
                    new Sprite(AssetsController.instance.getRegion("Angel_stand")),
                    new Sprite(AssetsController.instance.getRegion("Sniper_stand")),
                    new Sprite(AssetsController.instance.getRegion("Wizard_stand"))
            },
            {
                    new Sprite(AssetsController.instance.getRegion("Sparda_stand")),
                    new Sprite(AssetsController.instance.getRegion("Protector_stand")),
                    new Sprite(AssetsController.instance.getRegion("Angel_stand")),
                    new Sprite(AssetsController.instance.getRegion("Sniper_stand")),
                    new Sprite(AssetsController.instance.getRegion("Wizard_stand"))
            },
            {
                    new Sprite(AssetsController.instance.getRegion("Sparda_stand")),
                    new Sprite(AssetsController.instance.getRegion("Protector_stand")),
                    new Sprite(AssetsController.instance.getRegion("Angel_stand")),
                    new Sprite(AssetsController.instance.getRegion("Sniper_stand")),
                    new Sprite(AssetsController.instance.getRegion("Wizard_stand"))
            },
            {
                    new Sprite(AssetsController.instance.getRegion("Sparda_stand")),
                    new Sprite(AssetsController.instance.getRegion("Protector_stand")),
                    new Sprite(AssetsController.instance.getRegion("Angel_stand")),
                    new Sprite(AssetsController.instance.getRegion("Sniper_stand")),
                    new Sprite(AssetsController.instance.getRegion("Wizard_stand"))
            }
    };

    private Sprite blueTeamHero[][] = {
            {
                    new Sprite(AssetsController.instance.getRegion("Sparda_stand")),
                    new Sprite(AssetsController.instance.getRegion("Protector_stand")),
                    new Sprite(AssetsController.instance.getRegion("Angel_stand")),
                    new Sprite(AssetsController.instance.getRegion("Sniper_stand")),
                    new Sprite(AssetsController.instance.getRegion("Wizard_stand"))
            },
            {
                    new Sprite(AssetsController.instance.getRegion("Sparda_stand")),
                    new Sprite(AssetsController.instance.getRegion("Protector_stand")),
                    new Sprite(AssetsController.instance.getRegion("Angel_stand")),
                    new Sprite(AssetsController.instance.getRegion("Sniper_stand")),
                    new Sprite(AssetsController.instance.getRegion("Wizard_stand"))
            },
            {
                    new Sprite(AssetsController.instance.getRegion("Sparda_stand")),
                    new Sprite(AssetsController.instance.getRegion("Protector_stand")),
                    new Sprite(AssetsController.instance.getRegion("Angel_stand")),
                    new Sprite(AssetsController.instance.getRegion("Sniper_stand")),
                    new Sprite(AssetsController.instance.getRegion("Wizard_stand"))
            },
            {
                    new Sprite(AssetsController.instance.getRegion("Sparda_stand")),
                    new Sprite(AssetsController.instance.getRegion("Protector_stand")),
                    new Sprite(AssetsController.instance.getRegion("Angel_stand")),
                    new Sprite(AssetsController.instance.getRegion("Sniper_stand")),
                    new Sprite(AssetsController.instance.getRegion("Wizard_stand"))
            }
    };

    private void drawSite(){
        font.setColor(Color.BLACK);
        font.getData().setScale(0.0009f*width);
        for(int i = 0;i < room.getPlayerManager().getRedPlayerList().size;i ++){
            int heroType = room.getPlayerManager().getRedPlayerList().get(i).getHeroType();
            redTeamHero[i][heroType].setSize((heroWidth[heroType]/900)*width,(heroHeight[heroType]/500)*height);
            redTeamHero[i][heroType].setPosition(siteRed[i].getX()+siteRed[i].getWidth()/2-redTeamHero[i][heroType].getWidth()/2,
                    siteRed[i].getY()+siteRed[i].getHeight()/2+ 0.04f*height);
            redTeamHero[i][heroType].draw(batch);

            font.draw(batch,room.getPlayerManager().getRedPlayerList().get(i).getID(),siteRed[i].getX()+siteRed[i].getWidth()/2,siteRed[i].getY()+0.15f*height,0,1,true);

            int level = room.getPlayerManager().getRedPlayerList().get(i).getLevel();
            String levelShow = "";
            for(int j=0; j<level;j++){
                levelShow = levelShow + "★";
            }
            for(int j=0;j<(5-level);j++){
                levelShow = levelShow + "☆";
            }
            font.draw(batch,levelShow,siteRed[i].getX()+siteRed[i].getWidth()/2,siteRed[i].getY()+0.086f*height,0,1,true);
        }

        for(int i = 0;i < room.getPlayerManager().getBluePlayerList().size;i ++){
            int heroType = room.getPlayerManager().getBluePlayerList().get(i).getHeroType();
            blueTeamHero[i][heroType].setSize((heroWidth[heroType]/900)*width,(heroHeight[heroType]/500)*height);
            blueTeamHero[i][heroType].setPosition(siteBlue[i].getX()+siteBlue[i].getWidth()/2-blueTeamHero[i][heroType].getWidth()/2,
                    siteBlue[i].getY()+siteBlue[i].getHeight()/2+ 0.04f*height);
            blueTeamHero[i][heroType].draw(batch);

            font.draw(batch,room.getPlayerManager().getBluePlayerList().get(i).getID(),siteBlue[i].getX()+siteBlue[i].getWidth()/2,siteBlue[i].getY()+0.15f*height,0,1,true);

            int level = room.getPlayerManager().getBluePlayerList().get(i).getLevel();
            String levelShow = "";
            for(int j=0; j<level;j++){
                levelShow = levelShow + "★";
            }
            for(int j=0;j<(5-level);j++){
                levelShow = levelShow + "☆";
            }
            font.draw(batch,levelShow,siteBlue[i].getX()+siteBlue[i].getWidth()/2,siteBlue[i].getY()+0.086f*height,0,1,true);
        }
    }

    public void drawPersonNum(){
        font.setColor(Color.WHITE);
        String populationRed = room.getPlayerManager().getRedPlayerList().size + "/" + mode;
        String populationBlue = room.getPlayerManager().getBluePlayerList().size + "/" + mode;
        font.draw(batch,populationRed,doorRed.getX()+doorRed.getWidth()/2,doorRed.getY()+0.04f*height,1,1,true);
        font.draw(batch,populationBlue,doorBlue.getX()+doorBlue.getWidth()/2,doorBlue.getY()+0.04f*height,1,1,true);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 v = new Vector3(screenX, screenY, 0);
        camera.unproject(v);

        if(btnHeroLeft.getBoundingRectangle().contains(v.x,v.y) && !b_error){
            if (heroSelect > 0 && !myplayer.getReady()) {
                heroSelect --;
                myplayer.setHeroType(heroSelect);
                myplayer.setLevel(dc.getPersonalData(level[heroSelect]));
                game.getNetController().updatePlayer(myplayer);
            }
            AudioController.instance.play(AssetsController.instance.btnClicked);
        }

        if(btnHeroRight.getBoundingRectangle().contains(v.x,v.y) && !b_error){
            if(heroSelect < 4 && !myplayer.getReady()){
                heroSelect ++;
                myplayer.setHeroType(heroSelect);
                myplayer.setLevel(dc.getPersonalData(level[heroSelect]));
                game.getNetController().updatePlayer(myplayer);
            }
            AudioController.instance.play(AssetsController.instance.btnClicked);
        }

        if(btnMapLeft.getBoundingRectangle().contains(v.x,v.y)){
            if(mapNum > 0 && localHostIp.equals(room.getOwnerIp())){
                mapNum --;
                room.setMapName("" + mapNum);
                game.getNetController().chooseMap("" + mapNum);
            }
        }

        if(btnMapRight.getBoundingRectangle().contains(v.x,v.y)){
            if(mapNum < 1 && localHostIp.equals(room.getOwnerIp())){
                mapNum ++;
                room.setMapName("" + mapNum);
                game.getNetController().chooseMap("" + mapNum);
            }
        }

        if(btnToLobby.getBoundingRectangle().contains(v.x,v.y)){
            game.getNetController().quitRoom(myplayer);
            game.loadLobbyScreen();
        }

        if(btnReady.getBoundingRectangle().contains(v.x,v.y) && !b_error){
            if(myplayer.getIp().equals(room.getOwnerIp())){
                //游戏开始
                game.getNetController().startGame();
                game.loadGameScreen();
            }
            if(!myplayer.getIp().equals(room.getOwnerIp())){
                myplayer.setReady(true);
                game.getNetController().updatePlayer(myplayer);
            }
        }

        if(btnCancel.getBoundingRectangle().contains(v.x,v.y) && !b_error){
            myplayer.setReady(false);
            game.getNetController().updatePlayer(myplayer);
        }

        if(doorRed.getBoundingRectangle().contains(v.x,v.y) && !b_error){
            if(myplayer.getTeam() == Constants.PLAYER.BLUE_TEAM && !myplayer.getReady()){
                room.switchTeam(myplayer);
                game.getNetController().updatePlayer(myplayer);
            }
        }

        if(doorBlue.getBoundingRectangle().contains(v.x,v.y) && !b_error){
            if(myplayer.getTeam() == Constants.PLAYER.RED_TEAM && !myplayer.getReady()){
                room.switchTeam(myplayer);
                game.getNetController().updatePlayer(myplayer);
            }
        }
        return false;
    }

    public void drawError(){
        font.setColor(Color.RED);
        font.draw(batch,"<-- Owner Has Lost!Please Quit!",
                btnToLobby.getX()+200/900f*width,
                btnToLobby.getY()+btnToLobby.getHeight()/2,1,1,true);
    }

    public void errorQuit(){
        b_error = true;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    public Room getRoom(){
        return room;
    }
}