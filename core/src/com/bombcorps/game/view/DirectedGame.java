package com.bombcorps.game.view;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.controller.WorldController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.Room;
import com.bombcorps.game.model.World;

public abstract class DirectedGame implements ApplicationListener {
    protected boolean init;
    protected AbstractGameScreen currScreen;
    protected AbstractGameScreen nextScreen;
    protected FrameBuffer currFbo;
    protected FrameBuffer nextFbo;
    protected SpriteBatch batch;
    protected float t;
    protected ScreenTransition screenTransition;

    protected MenuScreen menuScreen;
    protected LobbyScreen lobbyScreen;
    protected RoomScreen roomScreen;
    protected GameScreen gameScreen;
    protected InfoScreen infoScreen;
    protected WorldController worldController;
    protected NetController netController = new NetController();

    public void setScreen(AbstractGameScreen screen) {
        setScreen(screen, null);
    }

    public void loadLobbyScreen(){
        lobbyScreen = new LobbyScreen(this);
        netController.bindGame(this);
        setScreen(lobbyScreen);
        Constants.CurrentScreenFlag = Constants.LobbyScreenFlag;
    }

    public void loadRoomScreen(Room room){
        roomScreen = new RoomScreen(this,NetController.getLocalHostIp(),room.getLIMIT(),room);
        netController.bindGame(this);
        setScreen(roomScreen);
        Constants.CurrentScreenFlag = Constants.RoomScreenFlag;
    }

//    public void loadinRoomScreen(int roomNum){
//        String ip =netController.getRoomList().get(roomNum).getOwnerIp();
//        int mode = netController.getRoomList().get(roomNum).getLIMIT();
//        roomScreen = new RoomScreen(this,ip,mode);
//        setScreen(roomScreen);
//        Constants.CurrentScreenFlag = Constants.RoomScreenFlag;
//    }

    public void loadGameScreen(){
        OrthographicCamera orthographicCamera =new OrthographicCamera();
        orthographicCamera.viewportHeight =10;
        orthographicCamera.viewportWidth = 18;
        //netController = new NetController();

        worldController = new WorldController(this,orthographicCamera,netController);

        Gdx.input.setInputProcessor(worldController.getInputProcessor());

        gameScreen = new GameScreen(this,worldController);
        setScreen(gameScreen);
        Constants.CurrentScreenFlag = Constants.GameScreenFlag;
    }

    public void loadInfoScreen(){
        infoScreen = new InfoScreen(this);
        setScreen(infoScreen);
        Constants.CurrentScreenFlag = Constants.InfoScreenFlag;
    }

    public void loadMenuScreen(){
        menuScreen= new MenuScreen(this);
        setScreen(menuScreen);
        Constants.CurrentScreenFlag =Constants.MenuScreenFlag;
    }

    public void onHeroClicked(Player p){
        gameScreen.onHeroClicked(p);
    }

    public void playerQuit(String Ip){
        //调用GameScreen里面的playequit

        gameScreen.playQuit(Ip);
    }

    public void errorStop(){
        //调用
        gameScreen.errorStop();
    }

    public void errorQuit(){
//        roomScreen.errorQuit();
    }

    public World getWorld(){

        World world = new World(roomScreen.getRoom());
        return world;
    }

    public boolean inRoom(){
        if(Constants.CurrentScreenFlag== Constants.RoomScreenFlag){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean hasRoom(){
        if(inRoom()){
            if (roomScreen.getRoom().getOwnerIp().equals(NetController.getLocalHostIp())){
                return true;
            }
        }
        return false;
    }

    public void updateLobbyScreen() {
        lobbyScreen.rebulidStage();
    }

    public void updateRoomScreen() {
//        roomScreen.rebulidStage();
    }

    public Room getRoom(){
        return roomScreen.getRoom();
    }

    public NetController getNetController(){
        return netController;
    }


    public void setScreen(AbstractGameScreen screen,
                          ScreenTransition screenTransition) {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        if (!init) {
            currFbo = new FrameBuffer(Format.RGB888, w, h, false);
            nextFbo = new FrameBuffer(Format.RGB888, w, h, false);
            batch = new SpriteBatch();
            init = true;
        }
        // 启动一个新的切换
        nextScreen = screen;
        nextScreen.show(); // 激活下一个屏幕
        nextScreen.resize(w, h);
        nextScreen.render(0); // 让下一个屏幕更新一次
        if (currScreen != null)
            currScreen.pause();
        nextScreen.pause();
        Gdx.input.setInputProcessor(null); // 禁止输入
        this.screenTransition = screenTransition;
        t = 0;
    }

    @Override
    public void render() {
        // get delta time and ensure an upper limit of one 60th second
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);
        if (nextScreen == null) {
            // no ongoing transition
            if (currScreen != null)
                currScreen.render(deltaTime);
        } else {
            // ongoing transition
            float duration = 0;
            if (screenTransition != null)
                duration = screenTransition.getDuration();
            // update progress of ongoing transition
            t = Math.min(t + deltaTime, duration);
            if (screenTransition == null || t >= duration) {
                // no transition effect set or transition has just finished
                if (currScreen != null)
                    currScreen.hide();
                nextScreen.resume();
                // enable input for next screen
                Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
                // switch screens
                currScreen = nextScreen;
                nextScreen = null;
                screenTransition = null;
            } else {
                // render screens to FBOs
                currFbo.begin();
                if (currScreen != null)
                    currScreen.render(deltaTime);
                currFbo.end();
                nextFbo.begin();
                nextScreen.render(deltaTime);
                nextFbo.end();
                // render transition effect to screen
                float alpha = t / duration;
                screenTransition.render(batch, currFbo.getColorBufferTexture(),
                        nextFbo.getColorBufferTexture(), alpha);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        if (currScreen != null)
            currScreen.resize(width, height);
        if (nextScreen != null)
            nextScreen.resize(width, height);
    }

    @Override
    public void pause() {
        if (currScreen != null)
            currScreen.pause();
    }

    @Override
    public void resume() {
        if (currScreen != null)
            currScreen.resume();
    }

    @Override
    public void dispose() {
        if (currScreen != null)
            currScreen.hide();
        if (nextScreen != null)
            nextScreen.hide();
        if (init) {
            currFbo.dispose();
            currScreen = null;
            nextFbo.dispose();
            nextScreen = null;
            batch.dispose();
            init = false;
        }
    }

}

