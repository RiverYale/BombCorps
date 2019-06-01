package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.PlayerManager;
import com.bombcorps.game.model.Room;

public class RoomScreen extends AbstractGameScreen{
    private static final String TAG = RoomScreen.class.getName();
    private static final float height = Gdx.graphics.getHeight();
    private static final float width = Gdx.graphics.getWidth();
    //背景图片
    private Image roomBackground;
    private Image doorBlue;
    private Image doorRed;
    private Image selectBackground;
    private Image siteRed[];
    private Image siteBlue[];
    //红蓝方队伍
    private SiteShow teamRed[];
    private SiteShow teamBlue[];
    //模式
    private int mode;
    private String ip;
    //红蓝方现有人数
    private int numOfRed;
    private int numOfBlue;
    //人数显示
    private Label personRed;
    private Label personBlue;
    //按钮
    private Image btnMapleft;
    private Image btnMapright;
    private Image btnHeroLeft;
    private Image btnHeroRight;
    private Image btnReady;
    private Image btnCancel;
    //英雄号码选择
    private int heroSelect;
    //地图号码
    private int mapNum = 0;
    private Image mapSelect;
    //英雄头像
    private Image hero[];
    //准备标识
    private boolean ready;

    private Stage stage;
    private Room room;

    public RoomScreen(DirectedGame game,String ip,int mode) {
        super(game);
        this.ip = ip;
        this.mode = mode;
        this.room = new Room(ip,mode);

        hero = new Image[5];
        hero[0] = new Image(new Texture("roomscreen/Angel_stand.png"));
        hero[1] = new Image(new Texture("roomscreen/Sparda_stand.png"));
        hero[2] = new Image(new Texture("roomscreen/Protector_stand.png"));
        hero[3] = new Image(new Texture("roomscreen/Sniper_stand.png"));
        hero[4] = new Image(new Texture("roomscreen/Wizard_stand.png"));
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        rebulidStage();
    }

    @Override
    public void hide() {
        stage.dispose();
    }

    @Override
    public void pause() {

    }

    public void rebulidStage(){
        //房间背景
        roomBackground = new Image(new Texture("roomscreen/roombackground.png"));
        roomBackground.setSize(width,height);
        //红蓝方门
        doorRed = new Image(new Texture(Gdx.files.internal("roomscreen/reddoor.png")));
        doorBlue = new Image(new Texture(Gdx.files.internal("roomscreen/bluedoor.png")));
        doorRed.setSize(0.2667f * width, 0.2f * height);
        doorRed.setPosition(0.37f * width, 0.73f * height);
        doorBlue.setSize(0.2667f * width, 0.2f * height);
        doorBlue.setPosition(doorRed.getX() + 0.3f * width, 0.73f * height);
        //空位背景设置
        Image site = new Image(new Texture("roomscreen/space.png"));
        site.setSize(doorRed.getWidth() / 2, 0.3f * height);
        site.setPosition(doorRed.getX(), doorRed.getY() - site.getHeight());
        float siteheight = site.getHeight();
        float sitewidth = site.getWidth();
        siteRed = new Image[4];
        siteBlue = new Image[4];
        for (int i = 0; i < 4; i++) {
            siteRed[i] = new Image(new Texture(Gdx.files.internal("site.png")));
            siteBlue[i] = new Image(new Texture(Gdx.files.internal("site.png")));
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
        //选择背景
        selectBackground = new Image(new Texture("roomscreen/selectbackground.png"));
        selectBackground.setSize(0.26667f * width,0.71f * height);
        selectBackground.setPosition(0.0444f * width,0.13f * height);

        stage.addActor(roomBackground);
        stage.addActor(doorRed);
        stage.addActor(doorBlue);
        stage.addActor(siteRed[0]);
        stage.addActor(siteRed[1]);
        stage.addActor(siteRed[2]);
        stage.addActor(siteRed[3]);
        stage.addActor(siteBlue[0]);
        stage.addActor(siteBlue[1]);
        stage.addActor(siteBlue[2]);
        stage.addActor(siteBlue[3]);
        stage.addActor(selectBackground);
        bulidTeam();
        drawButton();
        drawHero();
        drawMapSelect();
    }

    public void bulidTeam(){
        numOfRed = room.getPlayerManager().getRedPlayerList().size;
        numOfBlue = room.getPlayerManager().getBluePlayerList().size;
        teamRed = new SiteShow[numOfRed];
        teamBlue = new SiteShow[numOfBlue];
        //红队赋值
        for(int i = 0;i < numOfRed;i ++){
            teamRed[i] = new SiteShow(room.getPlayerManager().getRedPlayerList().get(i).getHeroType(),
                    room.getPlayerManager().getRedPlayerList().get(i).getID(),
                    room.getPlayerManager().getRedPlayerList().get(i).getLevel());
        }
        //蓝队赋值
        for(int i = 0;i < numOfBlue;i ++){
            teamBlue[i] = new SiteShow(room.getPlayerManager().getBluePlayerList().get(i).getHeroType(),
                    room.getPlayerManager().getBluePlayerList().get(i).getID(),
                    room.getPlayerManager().getBluePlayerList().get(i).getLevel());
        }
        //队伍贴图
        for(int i = 0;i < numOfRed;i ++){
            teamRed[i].setPosition(siteRed[i].getX() + siteRed[i].getWidth() / 2,siteRed[i].getY() + siteRed[i].getHeight()/2);
            teamRed[i].addToStage(stage);
        }
        for(int i = 0;i < numOfBlue;i ++){
            teamBlue[i].setPosition(siteBlue[i].getX() + siteBlue[i].getWidth() / 2,siteBlue[i].getY() + siteBlue[i].getHeight()/2);
            teamBlue[i].addToStage(stage);
        }
    }

    public void drawButton(){
        //英雄选择按钮
        btnHeroLeft = new Image(new Texture("roomscreen/heroleft.png"));
        btnHeroRight = new Image(new Texture("roomscreen/heroright.png"));
        btnHeroLeft.setSize(0.06667f * width,0.06f * height);
        btnHeroRight.setSize(0.06667f * width,0.06f * height);
        btnHeroLeft.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnMapleft.getWidth()/2- 0.08f*width,
                0.28f * height);
        btnHeroRight.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnMapright.getWidth()/2 + 0.08f * width,
                0.28f * height);
        stage.addActor(btnHeroLeft);
        stage.addActor(btnHeroRight);
        //地图选择按钮
        btnMapleft = new Image(new Texture("roomscreen/mapleft.png"));
        btnMapright = new Image(new Texture("roomscreen/mapright.png"));
        btnMapleft.setSize(0.06667f * width,0.08f * height);
        btnMapright.setSize(0.06667f * width,0.08f * height);
        btnMapleft.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnMapleft.getWidth()/2- 0.08f*width,
                0.4f * height);
        btnMapright.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnMapright.getWidth()/2 + 0.08f * width,
                0.4f * height);
        if(NetController.getLocalHostIp() == room.getOwnerIp()){
            stage.addActor(btnMapleft);
            stage.addActor(btnMapright);
        }

    }

    public void drawHero(){
        hero[heroSelect].setSize((hero[heroSelect].getWidth()/900)*width,(hero[heroSelect].getHeight()/500)*height);
        hero[heroSelect].setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - hero[heroSelect].getWidth()/2,
                btnHeroLeft.getY());
        stage.addActor(hero[heroSelect]);
    }

    public void drawMapSelect(){
        mapNum = Integer.parseInt(room.getMapName());
        mapSelect = new Image();
        switch(mapNum){
            case 0:
                mapSelect = new Image(new Texture("roomscreen/map0.png"));
                break;
        }
        stage.addActor(mapSelect);
    }

    public void addButtonListener(){
        //英雄
        btnHeroLeft.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){

            }
        });

        btnHeroRight.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){

            }
        });


    }
    public void setButtonClick(){

    }

    public void turnLeftHero(){

    }

    public void turnRightHero(){

    }

    public void turnLeftMap(){

    }

    public void turnRightMap(){

    }

    public void toRedTeam(){

    }

    public void toBlueTeam(){

    }

    public boolean isReady(){
        return ready;
    }

    public Room getRoom(){
        return room;
    }
}
