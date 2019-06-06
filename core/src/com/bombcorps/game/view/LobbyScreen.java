package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bombcorps.game.controller.AssetsController;

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
    //房间选项背景
    private TextureRegion roomItem[] =
                    {AssetsController.instance.getRegion("roomshowbackground"),
                    AssetsController.instance.getRegion("roomshowbackground"),
                    AssetsController.instance.getRegion("roomshowbackground"),
                    AssetsController.instance.getRegion("roomshowbackground")};

    private TextureRegion smallMap[] = new TextureRegion[4]; //小地图显示

    private BitmapFont font; //字体

    public LobbyScreen(DirectedGame game) {
        super(game);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(width, height);
        camera.position.set(width/2, height/2, 0);
        camera.update();
    }

    private void init(){
        font = AssetsController.instance.font;


    }


    @Override
    public InputProcessor getInputProcessor() {
        return null;
    }

    @Override
    public void render(float deltaTime) {

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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
