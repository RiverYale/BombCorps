package com.bombcorps.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
    private Image imgLogo;
    //private Image imgInfo;

    private Button btnMenuPlay;
    private Button btnMenuOptions;

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
        Table layerLogos = buildLogosLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();
        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        //stack.add(layerObjects);
        stack.add(layerLogos);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);


        }
    private Table buildBackgroundLayer(){
        Table layer = new Table();
        return layer;
    }
//    private Table buildObjectsLayer(){
//        Table layer = new Table();
//        return layer;
//    }
    private Table buildLogosLayer(){
        Table layer = new Table();
        return layer;
    }
    private Table buildControlsLayer(){
        Table layer = new Table();
        return layer;
    }
    private Table buildOptionsWindowLayer(){
        Table layer = new Table();
        return layer;
    }





}