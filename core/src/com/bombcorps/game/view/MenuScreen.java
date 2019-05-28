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

    @Override
    public InputProcessor getInputProcessor() {
        return null;
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
        //skinBombCorps.dispose();

    }
    @Override public void pause(){}

    private Stage stage;


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
    private Slider sldSound;
    private Slider sldMusic;
    private TextField tfName;



//    // debug
//    //private final float DEBUG_REBUILD_INTERVAL = 5.0f;
//    private boolean debugEnabled = false;
//    private float debugRebuildStage;

    private void rebulidStage(){
        // build all layers
        Table layerBackground = buildBackgroundLayer();
        Table layerControls = buildControlsLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();
        // assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBackground);
        stack.add(layerControls);
        stage.addActor(layerOptionsWindow);


    }
    private Table buildBackgroundLayer(){
        Table layer = new Table();
        imgBackground = new Image(new TextureRegion(new Texture(Gdx.files.internal("background1.png")),0,0,1024,576));
        layer.add(imgBackground);
        return layer;
    }
    private Table buildControlsLayer(){
        Table layer = new Table();
        //添加退出按钮
        btnQuit = new Image(new Texture(Gdx.files.internal("button_quit.png")));
        btnQuit.setScale(1.8f);
        layer.add(btnQuit);
        btnQuit.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });


        //添加play按钮
        btnMenuPlay = new Image(new Texture(Gdx.files.internal("button_start.png")));
        btnMenuPlay.setScale(1.8f);
        layer.bottom();
        layer.add(btnMenuPlay).padLeft(270);
        btnMenuPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onPlayClicked();
            }
        });
        //添加设置按钮
        btnMenuOptions = new Image(new Texture(Gdx.files.internal("button_setting.png")));
        btnMenuOptions.setScale(1.8f);
        layer.add(btnMenuOptions).padLeft(270);
        btnMenuOptions.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onOptionsClicked();
                return true;
            }
        });
        return layer;
    }
    private Table buildOptionsWindowLayer(){
        BitmapFont font =new BitmapFont(Gdx.files.internal("winOptions.fnt"),Gdx.files.internal("winOptions.png"),false);
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("window.png"))));
        winOptions = new Window("Options",windowStyle);
        winOptions.setSize(300,300);
        winOptions.add(buildOptWinAudioSettings()).row();
        winOptions.add(bulidOptWinNameSettings()).row();
        winOptions.add(buildOptWinButtons()).pad(5,0,10,0);
        //winOptions.setColor(1,1,1,1f);

        winOptions.setVisible(false);

        //winOptions.pack();
        winOptions.setPosition(Constants.VIEWPORT_GUI_WIDTH-winOptions.getWidth()-50,50);
        return winOptions;

    }
    private void onPlayClicked(){
        //切换到LobbyScreen
        //game.setScreen(new LobbyScreen(game));
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
        btnMenuPlay.setVisible(false);
        btnQuit.setVisible(false);
        btnMenuOptions.setVisible(false);
        winOptions.setVisible(true);
    }
    private Table buildOptWinAudioSettings(){
        Table tbl = new Table();
        //添加标题audio
        tbl.pad(10,10,0,10);
        BitmapFont font =new BitmapFont(Gdx.files.internal("winOptions.png"),Gdx.files.internal("winOptions.png"),false);
        tbl.add(new Label("Audio",new Label.LabelStyle(font,font.getColor()))).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        //添加sound标签 声音滑动控件
        tbl.add(new Label("Sound",new Label.LabelStyle(font,font.getColor())));

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(new TextureRegionDrawable(new Texture(Gdx.files.internal("sliderbackground.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("sliderbackground.png"))));

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
    private Table bulidOptWinNameSettings(){
        BitmapFont font =new BitmapFont(Gdx.files.internal("winOptions.fnt"),Gdx.files.internal("winOptions.png"),false);
        DataController prefs = DataController.instance;
        Table tbl = new Table();

        tbl.pad(10,10,0,10);
        Label lbl = new Label("change your name",new Label.LabelStyle(font, Color.WHITE));
        tbl.add(lbl);
        tbl.row();
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(font, Color.WHITE, new TextureRegionDrawable(new Texture(Gdx.files.internal("textfieldcusor.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("textfieldbackground.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("textfieldbackground.png"))));
        tfName = new TextField(prefs.getName(),textFieldStyle);
        tbl.add(tfName);
        return tbl;

    }
    private Table buildOptWinButtons(){
        Table tbl = new Table();
        //添加分割线

        BitmapFont font =new BitmapFont(Gdx.files.internal("winOptions.fnt"),Gdx.files.internal("winOptions.png"),false);
        Texture texture = new Texture(Gdx.files.internal("savecancelbutton .png"));

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