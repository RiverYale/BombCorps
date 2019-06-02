package com.bombcorps.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.bombcorps.game.controller.AudioController;
import com.bombcorps.game.controller.DataController;
import com.bombcorps.game.model.Constants;

public class MenuScreen extends AbstractGameScreen {

    private static final String TAG = MenuScreen.class.getName();

    public MenuScreen(DirectedGame game) {
        super(game);
    }

    //要修改
    @Override
    public InputProcessor getInputProcessor() {
        return stage;
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

        //Table.drawDebug(stage);
        stage.act();
        stage.draw();

    }
    @Override public void resize(int width,int height){
        stage.getViewport().update(width,height);
    }
    @Override public void show(){
        stage = new Stage();
        rebulidStage();

    }
    @Override public void hide(){
        stage.dispose();
        //skinBombCorps.dispose();

    }
    @Override public void pause(){}
    private float width = Gdx.graphics.getWidth();
    private float height = Gdx.graphics.getHeight();
    private Stage stage;


    // menu
    private Image imgBackground;
    //private Image imgLogo;
    //private Image imgInfo;

    private Image btnMenuPlay;
    private Image btnMenuOptions;
    private Image btnQuit;
    private Image btnAbout;

    // options
    private Window winOptions;
    private Window winAbout;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;
    private Slider sldSound;
    private Slider sldMusic;
    private TextField tfName;
    private final float DEBUG_REBUILD_INTERVAL = 5.0f;
    private boolean debugEnabled = false;
    private float debugRebuildStage;


//    // debug
//    //private final float DEBUG_REBUILD_INTERVAL = 5.0f;
//    private boolean debugEnabled = false;
//    private float debugRebuildStage;

    private void rebulidStage(){
        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();
        Table layerAboutWindow = bulidAboutWindowLayer();
        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stack.add(layerBackground);
        stack.addActor(layerControls);
        stage.addActor(layerOptionsWindow);
        stage.addActor(layerAboutWindow);
        //layerAboutWindow.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        layerAboutWindow.setPosition((Gdx.graphics.getWidth()-winAbout.getWidth())/2,(Gdx.graphics.getHeight()-winAbout.getHeight())/2);
       // layerOptionsWindow.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        layerOptionsWindow.setPosition((Gdx.graphics.getWidth()-winOptions.getWidth())/2,(Gdx.graphics.getHeight()-winOptions.getHeight())/2);

    }
    private Table buildBackgroundLayer(){
        Table layer = new Table();
        imgBackground = new Image(new TextureRegion(new Texture(Gdx.files.internal("images/background1.png"))));
        imgBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        layer.addActor(imgBackground);
        return layer;
    }
    private Table buildControlsLayer(){
        Table layer = new Table();
        //添加退出按钮
        btnQuit = new Image(new Texture(Gdx.files.internal("images/button_quit.png")));
        btnQuit.setScale(1.8f*Gdx.graphics.getWidth()/1280);

        layer.addActor(btnQuit);

        btnQuit.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });


        //添加play按钮
        btnMenuPlay = new Image(new Texture(Gdx.files.internal("images/button_start.png")));
        btnMenuPlay.setScale(1.8f*Gdx.graphics.getWidth()/1280);

        layer.addActor(btnMenuPlay);
        btnMenuPlay.setPosition(Gdx.graphics.getWidth()/3-1.8f*Gdx.graphics.getWidth()/1280*btnMenuPlay.getWidth()/3,0);
        btnMenuPlay.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onPlayClicked();
                return true;
            }
        });
        //添加设置按钮
        btnMenuOptions = new Image(new Texture(Gdx.files.internal("images/button_setting.png")));
        btnMenuOptions.setScale(1.8f*Gdx.graphics.getWidth()/1280);

        btnMenuOptions.setPosition(2*Gdx.graphics.getWidth()/3-3.6f*Gdx.graphics.getWidth()/1280*btnMenuPlay.getWidth()/3,0);
        btnMenuOptions.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onOptionsClicked();
                return true;
            }
        });
        layer.addActor(btnMenuOptions);
        //添加About按钮
        btnAbout = new Image(new Texture(Gdx.files.internal("images/button_about.png")));
        btnAbout.setScale(1.8f*Gdx.graphics.getWidth()/1280);
        layer.addActor(btnAbout);
        btnAbout.setPosition(Gdx.graphics.getWidth()-btnAbout.getWidth()*1.8f*Gdx.graphics.getWidth()/1280,0);
        btnAbout.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onAboutClicked();
                return true;
            }
        });
        return layer;
    }
    private Table buildOptionsWindowLayer(){

        BitmapFont font =new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("images/window.png"))));
        winOptions = new Window("Options",windowStyle);
        winOptions.add(buildOptWinAudioSettings()).row();
        winOptions.add(buildOptWinNameSettings()).row();
        winOptions.add(buildOptWinButtons()).pad(5,0,10,0);
        buildOptWinButtons().setSize(buildOptWinButtons().getPrefWidth(),buildOptWinButtons().getPrefHeight());


        buildOptWinButtons().debug();
        buildOptWinNameSettings().debug();
        buildOptWinAudioSettings().debug();
        winOptions.debug();
        winOptions.setColor(1,1,1,1f);
        winOptions.setVisible(false);
        winOptions.pack();
        winOptions.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        return winOptions;



    }
    private  Table bulidAboutWindowLayer(){

        BitmapFont font =new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        TextureRegionDrawable winResultsDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("images/winresult.png")));
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),winResultsDrawable);
        winAbout = new Window("",windowStyle);
        font.getData().setScale(width/1280f,width/1280f);
        winAbout.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        Label about = new Label("PRODUCERS:\n Zichuan Zhao\n ZhongWei Liu\n WenXin Zhu\n Rui Chen\n YuXuan Qin" ,new Label.LabelStyle(font,Color.BLACK));
        winAbout.addActor(about);
        btnQuit = new Image(new Texture(Gdx.files.internal("images/button_quit.png")));
        btnQuit.setScale(1.8f*Gdx.graphics.getWidth()/1280);
        winAbout.addActor(btnQuit);
        btnQuit.setPosition(0,winAbout.getHeight()-btnQuit.getHeight()*1.8f*Gdx.graphics.getWidth()/1280);
        btnQuit.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                winAbout.setVisible(false);
                return true;
            }
        });
        about.setSize(winAbout.getWidth(),winAbout.getHeight());
        about.setAlignment(Align.center);
        //about.setPosition(winAbout.getWidth()/2.5f,winAbout.getHeight()/3);
        about.setPosition(0,0);
        winAbout.setVisible(false);

        return winAbout;
    }
    private void onPlayClicked(){
        //切换到LobbyScreen
        game.loadLobbyScreen();
    }
    private void loadSettings(){
        DataController prefs = DataController.instance;
        prefs.loadSettings();
        sldSound.setValue(prefs.getVolSound());
        sldMusic.setValue(prefs.getVolMusic());
        tfName.setText(prefs.getName());

    }
    private void saveSettings(){
        DataController prefs = DataController.instance;
        prefs.setVolSound(sldSound.getValue());
        prefs.setVolMusic(sldMusic.getValue());
        prefs.setName(tfName.getText());
        prefs.saveSettings();
    }
    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
    }
    private void onCancelClicked(){
        btnMenuPlay.setVisible(true);
        btnMenuOptions.setVisible(true);
        btnQuit.setVisible(true);
        winOptions.setVisible(false);
    }
    private void onOptionsClicked(){
        loadSettings();
        winOptions.setVisible(true);
    }
    private void onAboutClicked(){
        winAbout.setVisible(true);
    }
    private Table buildOptWinAudioSettings(){
        Table tbl = new Table();
        //添加标题audio
        tbl.pad(10,10,0,10);
        BitmapFont font =new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        tbl.add(new Label("Audio",new Label.LabelStyle(font,font.getColor()))).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        //添加sound标签 声音滑动控件
        tbl.add(new Label("Sound",new Label.LabelStyle(font,font.getColor())));

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(new TextureRegionDrawable(new Texture(Gdx.files.internal("images/sliderbackground.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("images/sliderkuai.png"))));

        sldSound = new Slider(0.0f,1.0f,0.1f,false,sliderStyle);
        tbl.add(sldSound);
        tbl.row();
        //添加music标签 音乐滑动控件
        tbl.add(new Label("Music",new Label.LabelStyle(font,font.getColor())));
        sldMusic = new Slider(0.0f,1.0f,0.1f,false,sliderStyle);
        tbl.add(sldMusic);
        tbl.row();
        return tbl;
    }
    private Table buildOptWinNameSettings(){
        BitmapFont font =new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        DataController prefs = DataController.instance;
        Table tbl = new Table();

        tbl.pad(10,10,0,10);
        Label lbl = new Label("change your name",new Label.LabelStyle(font, Color.WHITE));
        tbl.add(lbl);
        tbl.row();
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(font, Color.WHITE, new TextureRegionDrawable(new Texture(Gdx.files.internal("images/textfieldcusor.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("images/textfieldbackground.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("images/textfieldbackground.png"))));
        tfName = new TextField(prefs.getName(),textFieldStyle);
        tbl.add(tfName);
        return tbl;

    }
    private Table buildOptWinButtons(){
        Table tbl = new Table();
        //添加分割线

        BitmapFont font =new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        Texture texture = new Texture(Gdx.files.internal("images/savecancelbutton.png"));

        //添加save按钮并且 初始化事件处理器
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(texture);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(textureRegionDrawable,textureRegionDrawable,textureRegionDrawable,font
        );
        btnWinOptSave = new TextButton("Save",textButtonStyle);

        tbl.add(btnWinOptSave).padLeft(20);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
        // 添加cancel按钮并且 初始化事件处理器
        btnWinOptCancel = new TextButton("Cancel", textButtonStyle);
        tbl.add(btnWinOptCancel).padRight(20);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCancelClicked();
            }
        });
        return tbl;

    }






}