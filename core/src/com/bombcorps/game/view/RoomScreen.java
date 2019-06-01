package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.bombcorps.game.model.Room;

public class RoomScreen extends AbstractGameScreen{
    private static final String TAG = RoomScreen.class.getName();
    private static final float height = Gdx.graphics.getHeight();
    private static final float width = Gdx.graphics.getWidth();
    //背景图片
    private Image roomBackground;
    private Image doorBlue;
    private Image doorRed;
    private Image teamBackground;
    private Image selectBackground;
    private Image siteRed[];
    private Image siteBlue[];
    //红蓝方队伍
    private SiteShow teamRed[];
    private SiteShow teamBlue[];
    //模式
    private int mode;
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
    //英雄号码选择
    private int heroSelect;
    //英雄头像
    private Image hero[];

    private Stage stage;

    public RoomScreen(DirectedGame game,int mode) {
        super(game);
        this.mode = mode;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return stage;
    }

    @Override
    public void render(float deltaTime) {

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
    }

    public void bulidTeam(){
        Room room = new Room(mode);
        //room.
    }
}
