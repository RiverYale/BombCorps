package com.bombcorps.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.bombcorps.game.model.Constants;

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        if (debugEnabled) {
////            debugRebuildStage -= deltaTime;
////            if (debugRebuildStage <= 0) {
////                debugRebuildStage = DEBUG_REBUILD_INTERVAL;
////                rebuildStage();
////            }
////        }
        stage.act(deltaTime);
        stage.draw();
        //Table.drawDebug(stage);
    }
    @Override public void resize(int width,int height){
        stage.getViewport().update(width,height);
    }
    @Override public void show(){
        stage = new Stage(new StretchViewport(
                Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebulidStage();
    }
    @Override public void hide(){
        stage.dispose();
        skinBombCorps.dispose();
    }
    @Override public void pause(){}

    private Stage stage;
    private Skin skinBombCorps;

    // menu
    private Image imgBackground;
    //private Image imgLogo;
    //private Image imgInfo;

    private Image btnMenuPlay;
    private Image btnMenuOptions;
    private Image btnQuit;

    // options
    private Window winOptions;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private CheckBox chkSound;
    private Slider sldSound;
    private CheckBox chkMusic;
    private Slider sldMusic;
    private SelectBox selCharSkin;
    private Image imgCharSkin;

    private CheckBox chkShowFpsCounter;

//    // debug
//    //private final float DEBUG_REBUILD_INTERVAL = 5.0f;
//    private boolean debugEnabled = false;
//    private float debugRebuildStage;

    private void rebulidStage(){
        // build all layers
        Table layerBackground = buildBackgroundLayer();
        //Table layerObjects = buildObjectsLayer();
//      Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();
        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        //stack.add(layerObjects);
        //stack.add(layerLogos);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);


        }
    private Table buildBackgroundLayer(){
        Table layer = new Table();
        imgBackground = new Image(new TextureRegion(new Texture(Gdx.files.internal("background1.png")),0,0,1024,576));
        layer.add(imgBackground);
        return layer;
    }
//    private Table buildObjectsLayer(){
//        Table layer = new Table();
//        return layer;
//    }
//    private Table buildLogosLayer(){
//        Table layer = new Table();
//        return layer;
//    }
    private Table buildControlsLayer(){

        Table layer = new Table();
        layer.left().bottom();
        //添加退出按钮
        btnQuit = new Image(new Texture(Gdx.files.internal("button_quit.png")));
        btnQuit.setSize(80,40);
        layer.add(btnQuit);

        layer.row().expandY();
        //添加play按钮
        btnMenuPlay = new Image(new Texture(Gdx.files.internal("button_start.png")));
        btnMenuPlay.setSize(80,40);
        layer.add(btnMenuPlay);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        //添加设置按钮
        btnMenuOptions = new Image(new Texture(Gdx.files.internal("button_setting.png")));
        btnMenuOptions.setSize(80,40);
        layer.add(btnMenuOptions);


        return layer;
    }

    private Table buildOptionsWindowLayer(){
        Table layer = new Table();
        return layer;


    }
    private void onPlayClicked(){
        //切换到LObbyScreen
        //game.setScreen(new LobbyScreen(game));
    }





}