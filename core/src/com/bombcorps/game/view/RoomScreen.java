package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.DataController;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.Room;

//import sun.java2d.windows.GDIBlitLoops;

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
    private Label errorMsg;
    //按钮
    private Image btnMapleft;
    private Image btnMapright;
    private Image btnHeroLeft;
    private Image btnHeroRight;
    private Image btnReady;
    private Image btnCancel;
    //英雄号码选择
    private int heroSelect = Constants.SPARDA;
    //地图号码
    private int mapNum = 0;
    private Image mapSelect;
    //英雄头像
    private Image hero[];
    private float heroHeight[];
    private float heroWidth[];
    //准备标识

    private Stage stage;
    private SpriteBatch batch;

    private Room room;
    private Player myplayer;

    private int readyNum = 0;
    boolean b_error = false;

    private BitmapFont font = AssetsController.instance.font;
    private Label.LabelStyle stylePersonNum = new Label.LabelStyle(font, Color.WHITE);

    DataController dc = DataController.instance;

    private int level[];

    public RoomScreen(DirectedGame game,String ip,int mode,Room room) {
        super(game);
        this.game = game;
        this.ip = ip;
        this.mode = mode;
        this.room = room;
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        room.setMapName("0");
        myplayer = new Player(dc.getName());
        myplayer.setIp(NetController.getLocalHostIp());


        room.addPlayer(myplayer);


        for(int i = 0;i < room.getPlayerManager().getAllPlayerList().size;i ++){
            if(NetController.getLocalHostIp().equals(room.getPlayerManager().getAllPlayerList().get(i).getIp())){
                myplayer = room.getPlayerManager().getAllPlayerList().get(i);
                break;
            }
        }

        myplayer.setLevel(dc.getPersonalData(DataController.SPARDAR));
        Gdx.app.log("Level",""+myplayer.getLevel());
        if(!NetController.getLocalHostIp().equals(this.ip)){
            //Gdx.app.log("I am in","this room");
            game.getNetController().enterRoom(this.ip,myplayer);
        }

        //Gdx.app.log("Number",""+room.getPlayerManager().getAllPlayerList().size);

        level= new int[5];

        level[Constants.SPARDA] = DataController.SPARDAR;
        level[Constants.WIZARD] = DataController.WIZARD;
        level[Constants.SNIPER] = DataController.SNIPER;
        level[Constants.PROTECTOR] = DataController.PROTECTOR;
        level[Constants.ANGEL] = DataController.ANGEL;

        //myplayer.setHeroType(Constants.SPARDA);

        //Gdx.app.log("Level",""+dc.getPersonalData(DataController.SPARDAR));

        hero = new Image[5];
        hero[Constants.ANGEL] = new Image(AssetsController.instance.getRegion("Angel_stand"));
        hero[Constants.SPARDA] = new Image(AssetsController.instance.getRegion("Sparda_stand"));
        hero[Constants.PROTECTOR] = new Image(AssetsController.instance.getRegion("Protector_stand"));
        hero[Constants.SNIPER] = new Image(AssetsController.instance.getRegion("Sniper_stand"));
        hero[Constants.WIZARD] = new Image(AssetsController.instance.getRegion("Wizard_stand"));
        heroWidth = new float[5];
        heroHeight = new float[5];
        for(int i = 0;i < 5;i ++){
            heroWidth[i] = hero[i].getWidth();
            heroHeight[i] = hero[i].getHeight();
        }

        drawImage();
        drawButton();
        setButtonClick();
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batchAddImage();
        batchAddButton();
        batchAddHero();
        batchAddMap();
        batchAddPersonNum();
        bulidTeam();
        if(b_error){
            Gdx.app.log("zc", "owner quit");
            batchAddErrorMsg();
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {
        //stage = new Stage();
    }

    @Override
    public void hide() {
        batch.dispose();
        stage.dispose();
    }

    @Override
    public void pause() {

    }

    public  void drawImage(){
        //房间背景
        roomBackground = new Image(AssetsController.instance.getRegion("roombackground"));
        roomBackground.setSize(width,height);
        //红蓝方门
        doorRed = new Image(AssetsController.instance.getRegion("reddoor"));
        doorBlue = new Image(AssetsController.instance.getRegion("bluedoor"));
        doorRed.setSize(0.2667f * width, 0.2f * height);
        doorRed.setPosition(0.37f * width, 0.73f * height);
        doorBlue.setSize(0.2667f * width, 0.2f * height);
        doorBlue.setPosition(doorRed.getX() + 0.3f * width, 0.73f * height);
        stage.addActor(doorRed);
        stage.addActor(doorBlue);
        //空位背景设置
        Image site = new Image(AssetsController.instance.getRegion("space"));
        site.setSize(doorRed.getWidth() / 2, 0.3f * height);
        site.setPosition(doorRed.getX(), doorRed.getY() - site.getHeight());
        float siteheight = site.getHeight();
        float sitewidth = site.getWidth();
        siteRed = new Image[4];
        siteBlue = new Image[4];
        for (int i = 0; i < 4; i++) {
            siteRed[i] = new Image(AssetsController.instance.getRegion("space"));
            siteBlue[i] = new Image(AssetsController.instance.getRegion("space"));
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
        selectBackground = new Image(AssetsController.instance.getRegion("selectbackground"));
        selectBackground.setSize(0.26667f * width,0.71f * height);
        selectBackground.setPosition(0.0444f * width,0.13f * height);
    }

    public void batchAddImage(){
        roomBackground.draw(batch,1);
        selectBackground.draw(batch,1);
        siteRed[0].draw(batch,1);
        siteRed[1].draw(batch,1);
        siteRed[2].draw(batch,1);
        siteRed[3].draw(batch,1);
        siteBlue[0].draw(batch,1);
        siteBlue[1].draw(batch,1);
        siteBlue[2].draw(batch,1);
        siteBlue[3].draw(batch,1);
    }

    public void bulidTeam(){
        //Gdx.app.log("Number",""+room.getPlayerManager().getAllPlayerList().size);
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
            teamRed[i].addToBatch(batch);
        }
        for(int i = 0;i < numOfBlue;i ++){
            teamBlue[i].setPosition(siteBlue[i].getX() + siteBlue[i].getWidth() / 2,siteBlue[i].getY() + siteBlue[i].getHeight()/2);
            teamBlue[i].addToBatch(batch);
        }
    }

    public void drawButton(){
        //英雄选择按钮
        btnHeroLeft = new Image(AssetsController.instance.getRegion("heroleft"));
        btnHeroRight = new Image(AssetsController.instance.getRegion("heroright"));
        stage.addActor(btnHeroLeft);
        stage.addActor(btnHeroRight);
        btnHeroLeft.setSize(0.06667f * width,0.06f * height);
        btnHeroRight.setSize(0.06667f * width,0.06f * height);
        btnHeroLeft.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnHeroLeft.getWidth()/2- 0.08f*width,
                0.28f * height);
        btnHeroRight.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnHeroRight.getWidth()/2 + 0.08f * width,
                0.28f * height);
        //地图选择按钮
        btnMapleft = new Image(AssetsController.instance.getRegion("mapleft"));
        btnMapright = new Image(AssetsController.instance.getRegion("mapright"));
        btnMapleft.setSize(0.06667f * width,0.08f * height);
        btnMapright.setSize(0.06667f * width,0.08f * height);
        btnMapleft.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnMapleft.getWidth()/2- 0.08f*width,
                0.4f * height);
        btnMapright.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - btnMapright.getWidth()/2 + 0.08f * width,
                0.4f * height);
        stage.addActor(btnMapleft);
        stage.addActor(btnMapright);
        //返回房间列表按钮
        backToLobby = new Image(AssetsController.instance.getRegion("mapleft"));
        backToLobby.setSize(0.045f * width,0.07f * height);
        backToLobby.setPosition(0.0222f * width,0.91f * height);
        stage.addActor(backToLobby);
        //开始/取消/开始游戏按钮
        btnReady = new Image(AssetsController.instance.getRegion("ready"));
        btnCancel = new Image(AssetsController.instance.getRegion("cancel"));
        btnReady.setSize(0.07778f * width,0.06f*height);
        btnReady.setPosition(selectBackground.getX() + selectBackground.getWidth()/2-btnReady.getWidth()/2-0.06f*width,
                0.16f*height);
        stage.addActor(btnReady);
        btnCancel.setSize(0.07778f * width,0.06f*height);
        btnCancel.setPosition(selectBackground.getX() + selectBackground.getWidth()/2-btnCancel.getWidth()/2+0.06f*width,
                0.16f*height);
        stage.addActor(btnCancel);
    }

    public void batchAddButton(){
        //换边按钮(门)
        doorRed.draw(batch,1);
        doorBlue.draw(batch,1);
        //英雄选择按钮
        btnHeroLeft.draw(batch,1);
        btnHeroRight.draw(batch,1);
        //选择地图按钮
        if(NetController.getLocalHostIp().equals(room.getOwnerIp())){
            btnMapleft.draw(batch,1);
            btnMapright.draw(batch,1);
        }
        //准备人数
        readyNum = 0;
        for(int i = 0;i < room.getPlayerManager().getAllPlayerList().size;i ++){
            if(room.getPlayerManager().getAllPlayerList().get(i).getReady()){
                readyNum ++;
            }
        }
        //准备/取消按钮布置
        if(myplayer.getIp().equals(room.getOwnerIp()) && (readyNum == (2 * mode - 1))){
            btnReady.draw(batch,1);
        }

        if(!myplayer.getIp().equals(room.getOwnerIp()) && myplayer.getReady()){
            btnCancel.draw(batch,1);
        }

        if(!myplayer.getIp().equals(room.getOwnerIp())&& !myplayer.getReady()){
            btnReady.draw(batch,1);
        }
        //返回按钮
        backToLobby.draw(batch,1);
    }

    public void drawHero(){
        //Gdx.app.log("size",hero[heroSelect].getWidth()+""+hero[heroSelect].getHeight());
        hero[heroSelect].setSize((hero[heroSelect].getWidth()/900)*width,(hero[heroSelect].getHeight()/500)*height);
        hero[heroSelect].setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - hero[heroSelect].getWidth()/2,
                btnHeroLeft.getY());
        hero[1].setSize((hero[1].getWidth()/900)*width,(hero[1].getHeight()/500)*height);
        hero[1].setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - hero[1].getWidth()/2,
                btnHeroLeft.getY());
        hero[2].setSize((hero[2].getWidth()/900)*width,(hero[2].getHeight()/500)*height);
        hero[2].setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - hero[2].getWidth()/2,
                btnHeroLeft.getY());
        hero[3].setSize((hero[3].getWidth()/900)*width,(hero[3].getHeight()/500)*height);
        hero[3].setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - hero[3].getWidth()/2,
                btnHeroLeft.getY());
        hero[4].setSize((hero[4].getWidth()/900)*width,(hero[4].getHeight()/500)*height);
        hero[4].setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - hero[4].getWidth()/2,
                btnHeroLeft.getY());
    }

    public void batchAddHero(){
        hero[heroSelect].setSize((heroWidth[heroSelect]/900)*width,(heroHeight[heroSelect]/500)*height);
        hero[heroSelect].setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - hero[heroSelect].getWidth()/2,
                btnHeroLeft.getY());
        hero[heroSelect].draw(batch,1);
    }

    public void batchAddMap(){
        mapNum = Integer.parseInt(room.getMapName());
        mapSelect = new Image();
        switch(mapNum){
            case 0:
                mapSelect = new Image(AssetsController.instance.getRegion("scalemap0"));
                break;
            case 1:
                mapSelect = new Image(AssetsController.instance.getRegion("scalemap1"));
        }
        mapSelect.setSize(0.23f * width,0.3f * height);
        mapSelect.setPosition(selectBackground.getX() + selectBackground.getWidth()/2 - mapSelect.getWidth()/2,
                0.51f * height);
        //stage.addActor(mapSelect);
        mapSelect.draw(batch,1);
    }



    public void batchAddPersonNum(){
        String populationRed = room.getPlayerManager().getRedPlayerList().size + "/" + mode;
        String populationBlue = room.getPlayerManager().getBluePlayerList().size + "/" + mode;
        personRed = new Label(populationRed,stylePersonNum);
        personBlue = new Label(populationBlue,stylePersonNum);
        personRed.setSize(0.04888f * width,0.057f * height);
        personBlue.setSize(0.04888f * width,0.057f * height);
        personRed.setFontScale(0.001f * width,0.0015f * height);
        personBlue.setFontScale(0.001f * width,0.0015f * height);
        personRed.setPosition(doorRed.getX() + doorRed.getWidth()/2 - personRed.getWidth()/2,
                doorRed.getY());
        personBlue.setPosition(doorBlue.getX() + doorBlue.getWidth()/2 - personBlue.getWidth()/2,
                doorBlue.getY());
        //stage.addActor(personRed);
        //stage.addActor(personBlue);
        personRed.draw(batch,1);
        personBlue.draw(batch,1);
    }

    public void batchAddWinError(){
        if(!ownerQuit()){
            //winError.draw(batch,1);
            errorQuit();
        }
    }

    public void batchAddErrorMsg(){
        BitmapFont font = AssetsController.instance.font;
        Label.LabelStyle style = new Label.LabelStyle(font,Color.RED);
        errorMsg = new Label("<-- Owner Has Lost!Please Quit!",style);
        errorMsg.setPosition(backToLobby.getX()+0.05556f*width,backToLobby.getY() + backToLobby.getHeight()/2 -errorMsg.getHeight()/2);
        errorMsg.setFontScale(1.0f);
        errorMsg.draw(batch,1);
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
            myplayer.setHeroType(heroSelect);
            myplayer.setLevel(dc.getPersonalData(level[heroSelect]));
            game.getNetController().updatePlayer(myplayer);
        }
    }

    public void turnRightHero(){
        if(heroSelect < 4 && !myplayer.getReady()){
            heroSelect ++;
            myplayer.setHeroType(heroSelect);
            myplayer.setLevel(dc.getPersonalData(level[heroSelect]));
            game.getNetController().updatePlayer(myplayer);
        }
    }

    public void turnLeftMap(){
        if(mapNum > 0 ){
            mapNum --;
            room.setMapName("" + mapNum);
            game.getNetController().chooseMap("" + mapNum);
        }
    }

    public void turnRightMap(){
        if(mapNum < 1){
            mapNum ++;
            room.setMapName("" + mapNum);
            game.getNetController().chooseMap("" + mapNum);
        }
    }

    public void toRedTeam(){
        if(myplayer.getTeam() == Constants.PLAYER.BLUE_TEAM && !myplayer.getReady()){
            room.switchTeam(myplayer);
            game.getNetController().updatePlayer(myplayer);
        }
    }

    public void toBlueTeam(){
        if(myplayer.getTeam() == Constants.PLAYER.RED_TEAM && !myplayer.getReady()){
            room.switchTeam(myplayer);
            game.getNetController().updatePlayer(myplayer);
        }
    }

    public void toReady(){
        //Gdx.app.log("intogame","Yes");
        if(myplayer.getIp().equals(room.getOwnerIp())){
            //游戏开始
            game.getNetController().startGame();
            game.loadGameScreen();
        }
        if(!myplayer.getIp().equals(room.getOwnerIp())){
            myplayer.setReady(true);
            game.getNetController().updatePlayer(myplayer);
        }

        readyNum = 0;
        for(int i = 0;i < room.getPlayerManager().getAllPlayerList().size;i ++){
            if(room.getPlayerManager().getAllPlayerList().get(i).getReady()){
                readyNum ++;
            }
        }
        Gdx.app.log("zc", readyNum+"");
    }

    public void toCancel(){
        myplayer.setReady(false);
        game.getNetController().updatePlayer(myplayer);
    }

    public Room getRoom(){
        return room;
    }

    public void toLobby(){
        game.getNetController().quitRoom(myplayer);
        game.loadLobbyScreen();
    }

    public void errorQuit(){
        //winError.setVisible(true);
        btnHeroLeft.setTouchable(Touchable.disabled);
        btnHeroRight.setTouchable(Touchable.disabled);
        btnReady.setTouchable(Touchable.disabled);
        btnCancel.setTouchable(Touchable.disabled);
        btnMapleft.setTouchable(Touchable.disabled);
        btnMapright.setTouchable(Touchable.disabled);
        doorRed.setTouchable(Touchable.disabled);
        doorBlue.setTouchable(Touchable.disabled);
        //errorMsg.setVisible(true);
        b_error = true;
    }

    public boolean ownerQuit(){
        for(int i = 0;i < room.getPlayerManager().getAllPlayerList().size;i ++){
            if(room.getPlayerManager().getAllPlayerList().get(i).getIp().equals(room.getOwnerIp())){
                return false;
            }
        }
        return true;
    }
}
