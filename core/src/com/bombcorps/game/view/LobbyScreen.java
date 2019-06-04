package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.DataController;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.Room;

import java.util.ArrayList;


public class LobbyScreen extends AbstractGameScreen{
    private static final String TAG = LobbyScreen.class.getName();
    private static final float height = Gdx.graphics.getHeight();
    private static final float width = Gdx.graphics.getWidth();

    private Image lsBackground; //大厅背景
    private Image roomListBackground;   //大厅列表背景

    private Label labelShowName;    //昵称
    private Label labelShowRate;    //胜率
    private Label labelShowWinAmount;   //胜场
    private Label labelShowProperty;    //金币数量

    private Image btnBuildRoom;     //创建房间按钮
    private Image btnRefresh;       //刷新房间按钮
    private Image btnPersonalIfo;   //个人信息按钮
    private Image btnBackToMenu;       //回到主界面按钮
    private Image btnLobbyOptions;  //大厅设置按钮
    private Image btnPageUp;        //列表上翻
    private Image btnPageDown;      //列表下翻
    private Window winBuildRoom;
    private Image oneOne;
    private Image twoTwo;
    private Image fourFour;
    private Image cancel;

    private int numOfRoom;      //房间个数
    private int numOfPage = 0;      //页数
    private ArrayList<Integer> originNum = new ArrayList<Integer>();

    private ArrayList<RoomSelect> roomList; //房间列表
   // private ArrayList<String> mode;

    private Stage stage;
    //private Stage stage2;
    //private SpriteBatch batch;

    private NetController netController;

    private DirectedGame game;

    public LobbyScreen(DirectedGame game) {
        super(game);
        this.game = game;
        netController = game.getNetController();
        netController.openReceiveMsgThread();
        roomList = new ArrayList<RoomSelect>();

       // batch = new SpriteBatch();
        //stage2 = new Stage();
        //Gdx.input.setInputProcessor(stage2);
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

        for(int i = 0;i < 4;i++){
            if(roomList.size() == 0 || (i + numOfPage*4 + 1) > roomList.size()){
                break;
            }
            //Gdx.app.log("i numOfPage",i+" "+numOfPage);
            if (roomList.get(i + numOfPage*4).isClick()){
                Gdx.app.log("Room","click");
                game.loadRoomScreen(game.getNetController().getRoomList().get(i + numOfPage));
            }
            roomList.get(i +numOfPage*4).update();
        }
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
        stage.clear();
        Gdx.input.setInputProcessor(stage);
        //大厅背景
        lsBackground = new Image(AssetsController.instance.getRegion("lsbackground"));
        lsBackground.setSize(width,height);
        //房间列表背景
        roomListBackground = new Image(AssetsController.instance.getRegion("roomlistbackground"));
        roomListBackground.setSize(0.53f * width,0.688f * height);
        roomListBackground.setPosition(0.45f * width,0.16f * height);
        //label字体风格
        BitmapFont font = AssetsController.instance.font;
        //font.setColor(1,1,1,1);
        Label.LabelStyle style = new Label.LabelStyle(font,Color.WHITE);

        Table recordTable = new Table();
        recordTable.clear();
        //Drawable recordbackground = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("lobbyscreen/recordbackground.png"))));
        Drawable recordbackground = new TextureRegionDrawable(new TextureRegion(AssetsController.instance.getRegion("recordbackground")));
        recordTable.setSize(0.35f * width,0.75f * height);
        recordTable.background(recordbackground);

        DataController dc = DataController.instance;

        String showName = "昵称:" +  dc.getName(); //+ 昵称
        labelShowName = new Label(showName,style);
        labelShowName.setFontScale(0.0018f * width);
        //labelShowName.debug();

        String showRate = "胜率:" + dc.getPersonalData(DataController.WIN_NUM)+"/"+dc.getPersonalData(DataController.GAME_NUM);  //+ 胜率
        labelShowRate = new Label(showRate,style);
        labelShowRate.setFontScale(0.0018f * width);

        String showWinAmount = "胜场:" + dc.getPersonalData(DataController.WIN_NUM);  //+ 胜场
        labelShowWinAmount = new Label(showWinAmount,style);
        labelShowWinAmount.setFontScale(0.0018f * width);

        String showProperty = "金币:" + dc.getPersonalData(DataController.MONEY); //+ 金币数
        labelShowProperty = new Label(showProperty,style);
        labelShowProperty.setFontScale(0.0018f * width);
        font.setColor(0,0,0,0);
        //个人信息布局
        recordTable.add(labelShowName).left().height(70);
        recordTable.row();
        recordTable.add(labelShowWinAmount).left().height(70);
        recordTable.row();
        recordTable.add(labelShowRate).left().height(70);
        recordTable.row();
        recordTable.add(labelShowProperty).left().height(70);
        Stack stackRecord = new Stack();
        recordTable.setPosition(0.08f * width,0.15f * height);
        stackRecord.setSize(0.35f * width,0.75f * height);
        stackRecord.addActor(recordTable);
        //模式选择窗口
        //TextureRegionDrawable winBuildRoomDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menuscreen/winbuildroom.png"))));
        TextureRegionDrawable winBuildRoomDrawable = new TextureRegionDrawable(AssetsController.instance.getRegion("winresult"));
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),winBuildRoomDrawable);
        winBuildRoom = new Window("",windowStyle);
        winBuildRoom.setSize(width/2,height/2);
        winBuildRoom.setPosition(width/4,height/4);
        cancel = new Image(AssetsController.instance.getRegion("winbuildroomcancel"));

//        Player myplayer = new Player(dc.getName());
//        myplayer.setIp(NetController.getLocalHostIp());

        oneOne = new Image(AssetsController.instance.getRegion("oneone"));

        twoTwo = new Image(AssetsController.instance.getRegion("twotwo"));
        fourFour = new Image(AssetsController.instance.getRegion("fourfour"));
        oneOne.setSize(winBuildRoom.getWidth()/4,winBuildRoom.getHeight()/4);
        twoTwo.setSize(winBuildRoom.getWidth()/4,winBuildRoom.getHeight()/4);
        fourFour.setSize(winBuildRoom.getWidth()/4,winBuildRoom.getHeight()/4);
        oneOne.setPosition(winBuildRoom.getWidth()/16,winBuildRoom.getHeight()/2);
        twoTwo.setPosition(winBuildRoom.getWidth()*6/16,winBuildRoom.getHeight()/2);
        fourFour.setPosition(winBuildRoom.getWidth()*11/16,winBuildRoom.getHeight()/2);
        cancel.setSize(winBuildRoom.getWidth()/4,winBuildRoom.getHeight()/4);
        cancel.setPosition(winBuildRoom.getWidth()*3/8,winBuildRoom.getHeight()/8);

        cancel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                winBuildRoom.setVisible(false);
                return true;
            }
        });
        oneOne.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Room room = new Room(NetController.getLocalHostIp(),1);
                game.loadRoomScreen(room);
                //game.getNetController().enterRoom(NetController.getLocalHostIp(),myplayer);
                return true;
            }
        });
        twoTwo.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Room room = new Room(NetController.getLocalHostIp(),2);
                game.loadRoomScreen(room);
                //game.getNetController().enterRoom(NetController.getLocalHostIp(),myplayer);
                return true;
            }
        });
        fourFour.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Room room = new Room(NetController.getLocalHostIp(),4);
                game.loadRoomScreen(room);
                //game.getNetController().enterRoom(NetController.getLocalHostIp(),myplayer);
                return true;
            }
        });

        winBuildRoom.addActor(oneOne);
        winBuildRoom.addActor(twoTwo);
        winBuildRoom.addActor(fourFour);
        winBuildRoom.addActor(cancel);
        stage.addActor(lsBackground);
        stage.addActor(roomListBackground);
        stage.addActor(recordTable);

        bulidRoomList();
        drawRoomList();
        drawButton();
        stage.addActor(winBuildRoom);
        winBuildRoom.setVisible(false);
    }
    //建造房间列表
    public void bulidRoomList(){
        //网端获取房间数numOfRoom
        numOfRoom = 0;
        numOfRoom  = game.getNetController().getRoomList().size();
        Gdx.app.log("numofroom",""+numOfRoom);
        roomList.clear();
        for(int i=0;i<numOfRoom;i++){
            if (!netController.getRoomList().get(i).isFull()){
                int mapNum = Integer.parseInt(netController.getRoomList().get(i).getMapName());
                String mode = netController.getRoomList().get(i).getLIMIT() + "vs" + netController.getRoomList().get(i).getLIMIT();
                String hostName = "";  //
                for(int j=0;j<netController.getRoomList().get(i).getPlayerManager().getAllPlayerList().size;j++){
                    if(netController.getRoomList().get(i).getPlayerManager().getAllPlayerList().get(j).getIp().equals(netController.getRoomList().get(i).getOwnerIp())){
                        hostName = netController.getRoomList().get(i).getPlayerManager().getAllPlayerList().get(j).getID();
                        break;
                    }
                }
                String personNum = netController.getRoomList().get(i).getPlayerManager().getAllPlayerList().size +
                        "/" + netController.getRoomList().get(i).getLIMIT()*2;
                roomList.add(new RoomSelect(mapNum,mode,hostName,personNum));
                originNum.add(i);
            }
        }
        numOfRoom = roomList.size();
    }

    //显示房间列表
    public void drawRoomList(){
        //stage2.clear();
        float originX = width * 0.488f;
        float originY = height * 0.655f;
        float intervalY = height * 0.105f;
        for(int i = 0;i < 4;i ++) {
            if ((i + numOfPage*4 + 1) > roomList.size()) {
                break;
            }
            roomList.get(i + numOfPage*4).setPosition(originX,originY - i * intervalY);
            roomList.get(i + numOfPage*4).addToStage(stage);
            //roomList.get(i + numOfPage*4).addToBatch(batch);
        }
//        for(int i = 0;i < 4;i++){
//            if(roomList.size() == 0 || (i + numOfPage*4 + 1) > roomList.size()){
//                break;
//            }
//            Gdx.app.log("i numOfPage",i+" "+numOfPage);
//            if (roomList.get(i + numOfPage*4).isClick()){
//                Gdx.app.log("Room","click");
//                game.loadRoomScreen(game.);
//            }
//            //roomList.get(i +numOfPage*4).update(Gdx.graphics.getDeltaTime());
//        }
    }

    //按键布局
    public void drawButton(){
        //创建房间按钮
        btnBuildRoom = new Image(AssetsController.instance.getRegion("home"));
        btnBuildRoom.setSize(0.1f * width,0.1f * height);
        btnBuildRoom.setPosition(0.54f * width,0.04f * height);
        //刷新房间列表按钮
        btnRefresh = new Image(AssetsController.instance.getRegion("refresh"));
        btnRefresh.setSize(0.1f * width,0.1f * height);
        btnRefresh.setPosition(btnBuildRoom.getX() + 0.13f * width,0.04f * height);
        //个人信息按钮
        btnPersonalIfo = new Image(AssetsController.instance.getRegion("user"));
        btnPersonalIfo.setSize(0.1f * width,0.1f * height);
        btnPersonalIfo.setPosition(btnBuildRoom.getX() + 0.26f * width,0.04f *height);

        //上一页按钮
        btnPageUp = new Image(AssetsController.instance.getRegion("pageup"));
        btnPageUp.setSize(0.0444f * width,0.06f * height);
        btnPageUp.setPosition(0.6222f * width,0.26f * height);
        //下一页按钮
        btnPageDown = new Image(AssetsController.instance.getRegion("pagedown"));
        btnPageDown.setSize(0.0444f * width,0.06f * height);
        btnPageDown.setPosition(btnPageUp.getX() + 0.15f * width,0.26f * height);
        //返回主页按钮
        btnBackToMenu = new Image(AssetsController.instance.getRegion("back"));
        btnBackToMenu.setSize(0.045f * width,0.07f * height);
        btnBackToMenu.setPosition(0.0222f * width,0.91f * height);
        //设置按钮
        btnLobbyOptions = new Image(AssetsController.instance.getRegion("button_setting"));
        btnLobbyOptions.setSize(0.045f * width,0.07f * height);
        btnLobbyOptions.setPosition(0.91f * width,0.91f * height);

        stage.addActor(btnBuildRoom);
        stage.addActor(btnRefresh);
        stage.addActor(btnPersonalIfo);
        stage.addActor(btnPageUp);
        stage.addActor(btnPageDown);
        stage.addActor(btnBackToMenu);
        //stage.addActor(btnLobbyOptions);

        setButtonClick();
    }

    //按键点击事件
    public void setButtonClick(){
        btnBuildRoom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                winBuildRoom.setVisible(true);
            }
        });

        btnRefresh.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x,float y){
               //刷新房间列表
               refreshRoomList();
           }
        });

        btnPersonalIfo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x,float y){
                //进入个人信息界面
                game.loadInfoScreen();
            }
        });

        btnPageUp.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x,float y){
                //上翻列表
                roomListPageUp();
            }
        });

        btnPageDown.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x,float y){
                //下翻列表
                roomListPageDown();
            }
        });

        btnBackToMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x,float y){
                //回到主界面
                game.loadMenuScreen();
            }
        });

        btnLobbyOptions.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x,float y){
                //弹出设置界面
            }
        });
    }

    //刷新房间
    public void refreshRoomList(){
        netController.refreshRoom();
        rebulidStage();
    }


    //房间列表上翻
    private void roomListPageUp(){
        if(numOfPage > 0){
            numOfPage --;
            //rebulidStage();
        }
    }

    //房间列表下翻
    private void roomListPageDown(){
        if(((numOfPage + 1) * 4 )< numOfRoom){
            numOfPage ++;
            //rebulidStage();
        }
    }
}
