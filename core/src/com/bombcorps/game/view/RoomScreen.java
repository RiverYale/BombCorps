package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.PlayerManager;
import com.bombcorps.game.model.Room;

public class RoomScreen extends AbstractGameScreen{
    private static final String TAG = RoomScreen.class.getName();
    private static final float height = Gdx.graphics.getHeight();
    private static final float width = Gdx.graphics.getWidth();
    private DirectedGame game;
    //背景图片
    private Image roomBackground;
    private Image doorBlue;
    private Image doorRed;
    private Image selectBackground;
    private Image siteRed[];
    private Image siteBlue[];
    private Image backToLobby;
    //红蓝方队伍
    private SiteShow teamRed[];
    private SiteShow teamBlue[];
    //异常退出弹窗
    private Window winError;
    private Image btnSure;
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

    private Player myplayer;

    private int readyNum = 0;

    public RoomScreen(DirectedGame game,String ip,int mode) {
        super(game);
        this.game = game;
        this.ip = ip;
        this.mode = mode;
        this.room = new Room(ip,mode);

        for(int i = 0;i < room.getPlayerManager().getAllPlayerList().size;i ++){
            if(NetController.getLocalHostIp() == room.getPlayerManager().getRedPlayerList().get(i).getIp()){
                myplayer = room.getPlayerManager().getAllPlayerList().get(i);
                break;
            }
        }

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
        showPersonNum();
        drawButton();
        drawHero();
        drawMapSelect();
        setButtonClick();
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

        backToLobby = new Image(new Texture("roomscreen/backtolobby.png"));
        backToLobby.setSize(0.045f * width,0.07f * height);
        backToLobby.setPosition(0.0222f * width,0.91f * height);
        stage.addActor(backToLobby);

        btnReady = new Image(new Texture("roomscreen/ready.png"));
        btnCancel = new Image(new Texture("roomscreen/cancel.png"));
        btnReady.setSize(0.07778f * width,0.06f*height);
        btnReady.setPosition(selectBackground.getX() + selectBackground.getWidth()/2-btnReady.getWidth()/2,
                0.16f*height);
        btnCancel.setSize(0.07778f * width,0.06f*height);
        btnCancel.setPosition(selectBackground.getX() + selectBackground.getWidth()/2-btnCancel.getWidth()/2,
                0.16f*height);

        backToLobby = new Image(new Texture("roomscreen/backtolobby.png"));
        backToLobby.setSize(0.045f * width,0.07f * height);
        backToLobby.setPosition(0.0222f * width,0.91f * height);

        for(int i = 0;i < room.getPlayerManager().getAllPlayerList().size;i ++){
            if(room.getPlayerManager().getRedPlayerList().get(i).getReady()){
                readyNum ++;
            }
        }
        if(myplayer.getIp() == room.getOwnerIp() && (readyNum >= (2 * mode - 1))){
            stage.addActor(btnReady);
        }

        if(myplayer.getIp() != room.getOwnerIp() && myplayer.getReady()){
            stage.addActor(btnCancel);
        }

        if(myplayer.getIp() != room.getOwnerIp() && !myplayer.getReady()){
            stage.addActor(btnReady);
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

    public void showPersonNum(){
        BitmapFont font = new BitmapFont(Gdx.files.internal("roomscreen/prosonnum.fnt"), Gdx.files.internal("roomscreen/prosonnum.png"), false);
        Label.LabelStyle style = new Label.LabelStyle(font, font.getColor());
        String populationRed = room.getPlayerManager().getRedPlayerList().size + "/" + mode;
        String populationBlue = room.getPlayerManager().getBluePlayerList().size + "/" + mode;
        personRed = new Label(populationRed,style);
        personBlue = new Label(populationBlue,style);
        personRed.setSize(0.04888f * width,0.057f * height);
        personBlue.setSize(0.04888f * width,0.057f * height);
        personRed.setFontScale(0.001f * width,0.0015f * height);
        personBlue.setFontScale(0.001f * width,0.0015f * height);
        personRed.setPosition(doorRed.getX() + doorRed.getWidth()/2 - personRed.getWidth()/2,
                doorRed.getY());
        personBlue.setPosition(doorBlue.getX() + doorBlue.getWidth()/2 - personBlue.getWidth()/2,
                doorBlue.getY());

        stage.addActor(personRed);
        stage.addActor(personBlue);
    }

    public void drawErrorWin(){
        BitmapFont font = new BitmapFont(Gdx.files.internal("roomscreen/site.fnt"), Gdx.files.internal("roomscreen/site.png"), false);
        Label.LabelStyle style = new Label.LabelStyle(font, font.getColor());
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("winresult.png"))));
        winError = new Window("",windowStyle);
        winError.setSize(width/2,height/2);
        winError.setPosition(width/4,height/4);
        Label label = new Label("HosterGone!",style);
        btnSure = new Image(new Texture("roomscreen/ready.png"));
        btnSure.setSize(0.07778f * width,0.06f*height);
        btnSure.setPosition(winError.getX() - btnSure.getWidth()/2,winError.getY()-0.3f*winError.getHeight());
        winError.addActor(label);
        winError.addActor(btnSure);
        stage.addActor(winError);
        winError.setVisible(false);

        btnSure.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x,float y){
                //下翻列表
                winError.setVisible(false);
            }
        });
    }

    public void setButtonClick(){
        //英雄选择
        btnHeroLeft.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                turnLeftHero();
            }
        });
        btnHeroRight.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                turnRightHero();
            }
        });
        //地图左右选择
        btnMapleft.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                turnLeftMap();
            }
        });
        btnMapright.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                turnRightMap();
            }
        });
        //换边
        doorRed.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                toRedTeam();
            }
        });
        doorBlue.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                toBlueTeam();
            }
        });
        btnReady.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                toReady();
            }
        });
        btnCancel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                toCancel();
            }
        });
        backToLobby.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                toLobby();
            }
        });
    }

    public void turnLeftHero() {
        if (heroSelect > 0 && !myplayer.getReady()) {
            heroSelect --;
        }
        rebulidStage();
    }

    public void turnRightHero(){
        if(heroSelect < 4 && !myplayer.getReady()){
            heroSelect ++;
        }
        rebulidStage();
    }

    public void turnLeftMap(){
        if(mapNum > 0 ){
            mapNum --;
        }
        rebulidStage();
    }

    public void turnRightMap(){
        if(mapNum < 0){
            mapNum ++;
        }
        rebulidStage();
    }

    public void toRedTeam(){
        if(myplayer.getTeam() == Constants.PLAYER.BLUE_TEAM && !myplayer.getReady()){
            room.switchTeam(myplayer);
        }
        rebulidStage();
    }

    public void toBlueTeam(){
        if(myplayer.getTeam() == Constants.PLAYER.RED_TEAM && !myplayer.getReady()){
            room.switchTeam(myplayer);
        }
        rebulidStage();
    }

    public void toReady(){
        if(myplayer.getIp() == room.getOwnerIp()){
            //游戏开始
            game.loadGameScreen();
        }
        if(myplayer.getIp() != room.getOwnerIp()){
            myplayer.setReady(true);
        }
    }

    public void toCancel(){
        myplayer.setReady(false);
    }

    public Room getRoom(){
        return room;
    }

    public void toLobby(){
        game.loadLobbyScreen();
    }

    public void errorQuit(){
        //回到房间列表
    }
}
