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
import com.bombcorps.game.model.Room;
import java.util.ArrayList;

public class LobbyScreen extends AbstractGameScreen implements InputProcessor{
    private float height = Gdx.graphics.getHeight();
    private float width = Gdx.graphics.getWidth();

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private TextureRegion lsBackground;  //大厅背景
    private TextureRegion myInfoBackground;  //个人信息背景
    private TextureRegion roomlistBackground;  //房间列表背景

    private Sprite btnToMenu;   //返回主页按钮
    private Sprite btnBuildRoom;    //创建房间按钮
    private Sprite btnRefresh;     //刷新房间按钮
    private Sprite btnToInfoScreen;     //个人信息按钮
    private Sprite btnRoomlistUp;   //列表上翻按钮
    private Sprite btnRoomlistDown; //列表下翻

    private Sprite btnOne;  //1v1
    private Sprite btnTwo;   //2v2
    private Sprite btnFour;  //4v4
    private Sprite btnCancel; //创建房间取消
    private Sprite winBuildRoom; //创建房间窗口
    private boolean showWinBR = false;

    private BitmapFont font; //字体

    private int numOfPage = 0;

    DataController dc = DataController.instance;
    private DirectedGame game;
    private NetController netController;

    public LobbyScreen(DirectedGame game) {
        super(game);
        this.game = game;
        netController = game.getNetController();
        batch = new SpriteBatch();
        camera = new OrthographicCamera(width, height);
        camera.position.set(width/2, height/2, 0);
        camera.update();
        init();
    }

    private void init(){
        font = AssetsController.instance.font;
        //大厅背景
        lsBackground = new TextureRegion(AssetsController.instance.getRegion("lsbackground"));
        //个人信息背景
        myInfoBackground = new TextureRegion(AssetsController.instance.getRegion("recordbackground"));
        //房间列表背景
        roomlistBackground = new TextureRegion(AssetsController.instance.getRegion("roomlistbackground"));
        //按钮布局
        //创建房间按钮
        btnBuildRoom = new Sprite(AssetsController.instance.getRegion("home"));
        btnBuildRoom.setSize(0.1f * width,0.1f * height);
        btnBuildRoom.setPosition(0.54f * width,0.04f * height);

        //刷新房间列表按钮
        btnRefresh = new Sprite(AssetsController.instance.getRegion("refresh"));
        btnRefresh.setSize(0.1f * width,0.1f * height);
        btnRefresh.setPosition(btnBuildRoom.getX() + 0.13f * width,0.04f * height);

        //个人信息按钮
        btnToInfoScreen = new Sprite(AssetsController.instance.getRegion("user"));
        btnToInfoScreen.setSize(0.1f * width,0.1f * height);
        btnToInfoScreen.setPosition(btnBuildRoom.getX() + 0.26f * width,0.04f *height);

        //上一页按钮
        btnRoomlistUp = new Sprite(AssetsController.instance.getRegion("pageup"));
        btnRoomlistUp.setSize(0.0444f * width,0.06f * height);
        btnRoomlistUp.setPosition(0.6222f * width,0.26f * height);

        //下一页按钮
        btnRoomlistDown = new Sprite(AssetsController.instance.getRegion("pagedown"));
        btnRoomlistDown.setSize(0.0444f * width,0.06f * height);
        btnRoomlistDown.setPosition(btnRoomlistUp.getX() + 0.15f * width,0.26f * height);

        //返回主页按钮
        btnToMenu = new Sprite(AssetsController.instance.getRegion("back"));
        btnToMenu.setSize(0.045f * width,0.07f * height);
        btnToMenu.setPosition(0.0222f * width,0.91f * height);

        //创建房间弹窗
        winBuildRoom = new Sprite(AssetsController.instance.getRegion("winresult"));
        winBuildRoom.setSize(width/2,height/2);
        winBuildRoom.setPosition(width/4,height/4);
        float btnX = winBuildRoom.getX();
        float btnY = winBuildRoom.getY();
        btnOne = new Sprite(AssetsController.instance.getRegion("oneone"));
        btnOne.setSize(winBuildRoom.getWidth()/4,winBuildRoom.getHeight()/4);
        btnOne.setPosition(btnX+winBuildRoom.getWidth()/16,btnY+winBuildRoom.getHeight()/2);

        btnTwo = new Sprite(AssetsController.instance.getRegion("twotwo"));
        btnTwo.setSize(winBuildRoom.getWidth()/4,winBuildRoom.getHeight()/4);
        btnTwo.setPosition(btnX+winBuildRoom.getWidth()*6/16,btnY+winBuildRoom.getHeight()/2);

        btnFour = new Sprite(AssetsController.instance.getRegion("fourfour"));
        btnFour.setSize(winBuildRoom.getWidth()/4,winBuildRoom.getHeight()/4);
        btnFour.setPosition(btnX+winBuildRoom.getWidth()*11/16,btnY+winBuildRoom.getHeight()/2);

        btnCancel = new Sprite(AssetsController.instance.getRegion("winbuildroomcancel"));
        btnCancel.setSize(winBuildRoom.getWidth()/4,winBuildRoom.getHeight()/4);
        btnCancel.setPosition(btnX+winBuildRoom.getWidth()*3/8,btnY+winBuildRoom.getHeight()/8);
    }


    @Override
    public InputProcessor getInputProcessor() {
        return this;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(lsBackground,0,0,width,height);
        batch.draw(myInfoBackground,0.08f * width,0.15f * height,0.35f*width,0.75f*height);
        batch.draw(roomlistBackground,0.45f * width,0.16f * height,0.53f * width,0.688f * height);

        drawMyInfo();
        drawButton();
        drawRoomList();
        if(showWinBR){
            drawBuildRoomWin();
        }

        batch.end();
    }

    public void drawMyInfo(){
        font.getData().setScale(0.0024f*height);
        font.setColor(Color.WHITE);
        font.draw(batch,"昵称: " + dc.getName(),3/18f * width,0.7f * height);
        font.draw(batch,"胜场: " + dc.getPersonalData(DataController.WIN_NUM)+"/"+dc.getPersonalData(DataController.GAME_NUM),3/18f * width,0.6f * height);
        font.draw(batch,"胜率: " + dc.getWinRate(),3/18f * width,0.5f * height);
        font.draw(batch,"金币: " + dc.getPersonalData(DataController.MONEY),3/18f * width,0.4f * height);
    }

    public void drawButton(){
        btnBuildRoom.draw(batch);
        btnRefresh.draw(batch);
        btnToInfoScreen.draw(batch);

        btnRoomlistUp.draw(batch);
        btnRoomlistDown.draw(batch);

        btnToMenu.draw(batch);
    }


    //房间选项背景
    private Sprite roomItem[] = {new Sprite(AssetsController.instance.getRegion("roomshowbackground")),
                                 new Sprite(AssetsController.instance.getRegion("roomshowbackground")),
                                 new Sprite(AssetsController.instance.getRegion("roomshowbackground")),
                                 new Sprite(AssetsController.instance.getRegion("roomshowbackground"))};

    private Sprite smallMap[][] = {
            {new Sprite(AssetsController.instance.getRegion("scalemap0")),new Sprite(AssetsController.instance.getRegion("scalemap1"))},
            {new Sprite(AssetsController.instance.getRegion("scalemap0")),new Sprite(AssetsController.instance.getRegion("scalemap1"))},
            {new Sprite(AssetsController.instance.getRegion("scalemap0")),new Sprite(AssetsController.instance.getRegion("scalemap1"))},
            {new Sprite(AssetsController.instance.getRegion("scalemap0")),new Sprite(AssetsController.instance.getRegion("scalemap1"))},
    };
    private ArrayList<Integer> unfullRoom = new ArrayList<Integer>();
    //private
    public void drawRoomList() {
        unfullRoom.clear();
        font.getData().setScale(0.0024f*height);
        float originX = width * 0.488f;
        float originY = height * 0.655f;
        float intervalY = height * 0.105f;
        for (int i = 0; i < netController.getRoomList().size(); i++) {
            if (!netController.getRoomList().get(i).isFull()) {
                unfullRoom.add(i);
            }
        }

        for(int i = 0;i < 4;i++){
            if((i + numOfPage * 4 + 1) > unfullRoom.size()){
                break;
            }
            roomItem[i].setSize(0.45f * width,0.1f * height);
            roomItem[i].setPosition(originX,originY - i * intervalY);
            roomItem[i].draw(batch);
            float middle = roomItem[i].getY() + roomItem[i].getHeight()/2;
            int roomNum = unfullRoom.get(i + numOfPage * 4);
            smallMap[i][Integer.parseInt(netController.getRoomList().get(roomNum).getMapName())].setSize(0.035f * width, 0.035f * width);
            smallMap[i][Integer.parseInt(netController.getRoomList().get(roomNum).getMapName())].setPosition(roomItem[i].getX()+ 0.027f * width,
                    middle - smallMap[i][Integer.parseInt(netController.getRoomList().get(roomNum).getMapName())].getHeight()/2);
            smallMap[i][Integer.parseInt(netController.getRoomList().get(roomNum).getMapName())].draw(batch);
            font.draw(batch,
                    netController.getRoomList().get(roomNum).getLIMIT() + "vs" + netController.getRoomList().get(roomNum).getLIMIT(),
                    roomItem[i].getX() + 0.075f * width,middle + 0.02f*height);
            String hostName = "";
            for(int j=0;j<netController.getRoomList().get(roomNum).getPlayerManager().getAllPlayerList().size;j++){
                if(netController.getRoomList().get(roomNum).getPlayerManager().getAllPlayerList().get(j).getIp().equals(netController.getRoomList().get(roomNum).getOwnerIp())){
                    hostName = netController.getRoomList().get(roomNum).getPlayerManager().getAllPlayerList().get(j).getID();
                    break;
                }
            }
            font.draw(batch, hostName,roomItem[i].getX() + 0.2f * width,middle + 0.02f*height);
            font.draw(batch,
                    netController.getRoomList().get(roomNum).getPlayerManager().getAllPlayerList().size + "/" + netController.getRoomList().get(roomNum).getLIMIT()*2,
                    roomItem[i].getX() + 0.33f * width,middle + 0.02f*height);
        }
    }

    public void drawBuildRoomWin(){
        winBuildRoom.draw(batch);
        btnOne.draw(batch);
        btnTwo.draw(batch);
        btnFour.draw(batch);
        btnCancel.draw(batch);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 v = new Vector3(screenX, screenY, 0);
        camera.unproject(v);

        if(btnToMenu.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            game.loadMenuScreen();
        }

        if(btnToInfoScreen.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            game.loadInfoScreen();
        }

        if(btnRefresh.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            netController.refreshRoom();
        }

        if(btnBuildRoom.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            showWinBR = true;
        }

        if(btnCancel.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            showWinBR = false;
        }
        //创建房间
        if(btnOne.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            Room room = new Room(NetController.getLocalHostIp(),1);
            game.loadRoomScreen(room);
        }
        if(btnTwo.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            Room room = new Room(NetController.getLocalHostIp(),2);
            game.loadRoomScreen(room);
        }
        if(btnFour.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            Room room = new Room(NetController.getLocalHostIp(),4);
            game.loadRoomScreen(room);
        }

        for(int i = 0;i < 4;i ++){
            if((i + numOfPage * 4 + 1) > netController.getRoomList().size()){
                break;
            }
            if(roomItem[i].getBoundingRectangle().contains(v.x,v.y)){
                game.loadRoomScreen(netController.getRoomList().get(unfullRoom.get(i + numOfPage * 4)));
            }
        }

        if(btnRoomlistUp.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            if(numOfPage > 0){
                numOfPage --;
            }
        }

        if(btnRoomlistDown.getBoundingRectangle().contains(v.x,v.y)){
            AudioController.instance.play(AssetsController.instance.btnClicked);
            if(((numOfPage + 1) * 4 ) < unfullRoom.size()){
                numOfPage ++;
            }
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        batch.dispose();
        camera = null;
    }

    @Override
    public void pause() {

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
}
