package com.bombcorps.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
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
import com.bombcorps.game.controller.AssetsController;
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
    private TextButton textButtonPlay;
    private TextButton textButtonSettings;
    private TextButton textButtonAbout;
    private TextButton textButtonQuit;
    private Slider sldSound;
    private Slider sldMusic;
    private TextField tfName;
    BitmapFont font = AssetsController.instance.font;




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
//        imgBackground = new Image(new TextureRegion(new Texture(Gdx.files.internal("menuscreen/background1.png"))));
        imgBackground = new Image(AssetsController.instance.getRegion("background1"));
        imgBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        layer.addActor(imgBackground);
        return layer;
    }
    private Table buildControlsLayer(){
        TextureRegion texture = new TextureRegion(new Texture("menuscreen/menutextbutton.png"));
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(texture);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(textureRegionDrawable,textureRegionDrawable,textureRegionDrawable,font
        );
        Table layer = new Table();
        //添加退出按钮
//        btnQuit = new Image(new Texture(Gdx.files.internal("menuscreen/button_quit.png")));
        btnQuit = new Image(AssetsController.instance.getRegion("button_quit"));
        float y = btnQuit.getHeight()*1.8f*Gdx.graphics.getWidth()/1280;
        float x = btnQuit.getWidth()*1.8f*Gdx.graphics.getWidth()/1280;
        btnQuit.setScale(1.8f*Gdx.graphics.getWidth()/1280);
        layer.addActor(btnQuit);
        btnQuit.setPosition(Gdx.graphics.getWidth()/10,2*Gdx.graphics.getHeight()/3-6*y);
        btnQuit.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();AudioController.instance.play(AssetsController.instance.btnClicked);
                return true;
            }
        });
        textButtonQuit = new TextButton("退出",textButtonStyle);
        layer.addActor(textButtonQuit);
        textButtonQuit.setPosition(Gdx.graphics.getWidth()/10+x,2*Gdx.graphics.getHeight()/3-6*y);
        textButtonQuit.setSize(3*x,y);
        textButtonQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();AudioController.instance.play(AssetsController.instance.btnClicked);
                return true;
            }
        });


        //添加play按钮
//        btnMenuPlay = new Image(new Texture(Gdx.files.internal("menuscreen/button_start.png")));
        btnMenuPlay = new Image(AssetsController.instance.getRegion("button_start"));
        btnMenuPlay.setScale(1.8f*Gdx.graphics.getWidth()/1280);

        layer.addActor(btnMenuPlay);
        btnMenuPlay.setPosition(Gdx.graphics.getWidth()/10,2*Gdx.graphics.getHeight()/3-y);
        btnMenuPlay.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onPlayClicked();AudioController.instance.play(AssetsController.instance.btnClicked);
                return true;
            }
        });
        textButtonPlay = new TextButton("开始游戏",textButtonStyle);
        layer.addActor(textButtonPlay);
        textButtonPlay.setPosition(Gdx.graphics.getWidth()/10+x,2*Gdx.graphics.getHeight()/3-y);
        textButtonPlay.setSize(3*x,y);
        textButtonPlay.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onPlayClicked();AudioController.instance.play(AssetsController.instance.btnClicked);
                return true;
            }
        });
        //添加设置按钮
//        btnMenuOptions = new Image(new Texture(Gdx.files.internal("menuscreen/button_setting.png")));
        btnMenuOptions = new Image(AssetsController.instance.getRegion("button_setting"));
        btnMenuOptions.setScale(1.8f*Gdx.graphics.getWidth()/1280);

        btnMenuOptions.setPosition(Gdx.graphics.getWidth()/10,2*Gdx.graphics.getHeight()/3-8*y/3);
        btnMenuOptions.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onOptionsClicked();AudioController.instance.play(AssetsController.instance.btnClicked);
                return true;
            }
        });
        layer.addActor(btnMenuOptions);
        textButtonSettings = new TextButton("设置",textButtonStyle);
        layer.addActor(textButtonSettings);
        textButtonSettings.setPosition(Gdx.graphics.getWidth()/10+x,2*Gdx.graphics.getHeight()/3-8*y/3);
        textButtonSettings.setSize(3*x,y);
        textButtonSettings.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onOptionsClicked();AudioController.instance.play(AssetsController.instance.btnClicked);
                return true;
            }
        });
        //添加About按钮
//        btnAbout = new Image(new Texture(Gdx.files.internal("menuscreen/button_about.png")));
        btnAbout = new Image(AssetsController.instance.getRegion("button_about"));
        btnAbout.setScale(1.8f*Gdx.graphics.getWidth()/1280);
        layer.addActor(btnAbout);
        btnAbout.setPosition(Gdx.graphics.getWidth()/10,2*Gdx.graphics.getHeight()/3-13*y/3);
        btnAbout.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onAboutClicked();AudioController.instance.play(AssetsController.instance.btnClicked);
                return true;
            }
        });
        textButtonAbout = new TextButton("关于",textButtonStyle);
        layer.addActor(textButtonAbout);
        textButtonAbout.setPosition(Gdx.graphics.getWidth()/10+x,2*Gdx.graphics.getHeight()/3-13*y/3);
        textButtonAbout.setSize(3*x,y);
        textButtonAbout.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onAboutClicked();AudioController.instance.play(AssetsController.instance.btnClicked);
                return true;
            }
        });
        return layer;
    }
    private Table buildOptionsWindowLayer(){

//        BitmapFont font =new BitmapFont(Gdx.files.internal("menuscreen/winOptions.fnt"), Gdx.files.internal("menuscreen/winOptions.png"),false)
//        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("menuscreen/window.png"))));
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(AssetsController.instance.getRegion("window")));
        winOptions = new Window("Options",windowStyle);
        winOptions.add(buildOptWinAudioSettings()).row();
        winOptions.add(buildOptWinNameSettings()).row();
        winOptions.add(buildOptWinButtons()).pad(5,0,10,0);
        winOptions.setColor(1,1,1,1f);
        winOptions.setVisible(false);
        winOptions.pack();
        winOptions.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        return winOptions;



    }
    private  Table bulidAboutWindowLayer(){

//        BitmapFont font =new BitmapFont(Gdx.files.internal("menuscreen/winOptions.fnt"),Gdx.files.internal("menuscreen/winOptions.png"),false);
//        TextureRegionDrawable winResultsDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("menuscreen/winresult.png")));
        TextureRegionDrawable winResultsDrawable = new TextureRegionDrawable(AssetsController.instance.getRegion("winresult"));
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),winResultsDrawable);
        winAbout = new Window("",windowStyle);
        font.getData().setScale(width/1280f,width/1280f);
        winAbout.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        Label about = new Label("制作者:\n 赵子川\n 刘忠伟\n 朱文鑫\n 陈锐\n 覃钰璇" ,new Label.LabelStyle(font,Color.BLACK));
        winAbout.addActor(about);
//        btnQuit = new Image(new Texture(Gdx.files.internal("menuscreen/button_quit.png")));
        btnQuit = new Image(AssetsController.instance.getRegion("button_quit"));
        btnQuit.setScale(1.8f*Gdx.graphics.getWidth()/1280);
        winAbout.addActor(btnQuit);
        btnQuit.setPosition(0,winAbout.getHeight()-btnQuit.getHeight()*1.8f*Gdx.graphics.getWidth()/1280);
        btnQuit.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                winAbout.setVisible(false);
                textButtonQuit.setVisible(true);
                textButtonAbout.setVisible(true);
                textButtonSettings.setVisible(true);
                textButtonPlay.setVisible(true);AudioController.instance.play(AssetsController.instance.btnClicked);
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
        textButtonQuit.setVisible(true);
        textButtonAbout.setVisible(true);
        textButtonSettings.setVisible(true);
        textButtonPlay.setVisible(true);

    }
    private void onCancelClicked(){
        winOptions.setVisible(false);
        textButtonQuit.setVisible(true);
        textButtonAbout.setVisible(true);
        textButtonSettings.setVisible(true);
        textButtonPlay.setVisible(true);
        AudioController.instance.onSettingsUpdated();
    }
    private void onOptionsClicked(){
        loadSettings();
        winOptions.setVisible(true);
        textButtonQuit.setVisible(false);
        textButtonAbout.setVisible(false);
        textButtonSettings.setVisible(false);
        textButtonPlay.setVisible(false);

    }
    private void onAboutClicked(){
        winAbout.setVisible(true);
        textButtonQuit.setVisible(false);
        textButtonAbout.setVisible(false);
        textButtonSettings.setVisible(false);
        textButtonPlay.setVisible(false);
    }
    private Table buildOptWinAudioSettings(){
        Table tbl = new Table();
        //添加标题audio
        tbl.pad(0,10,0,10);

//        BitmapFont font =new BitmapFont(Gdx.files.internal("menuscreen/winOptions.fnt"),Gdx.files.internal("menuscreen/winOptions.png"),false);
        Label audioLbl = new Label("Audio",new Label.LabelStyle(font,font.getColor()));
        audioLbl.setFontScale(1.3f*width/1280f);
        tbl.add(audioLbl).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        //添加sound标签 声音滑动控件

        Label soundLbl = new Label("Sound",new Label.LabelStyle(font,font.getColor()));
        soundLbl.setFontScale(1.3f*width/1280f);
        tbl.add(soundLbl).padTop(20*width/1280);
//        Texture sliderBac=new Texture("menuscreen/sliderbackground.png");
        TextureRegion sliderBac = AssetsController.instance.getRegion("sliderbackground");
        Image image = new Image(sliderBac);
        //TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(sliderBac);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(new TextureRegionDrawable(sliderBac),
//                new TextureRegionDrawable(new Texture(Gdx.files.internal("menuscreen/sliderkuai.png"))));
                new TextureRegionDrawable(AssetsController.instance.getRegion("sliderkuai")));

        sldSound = new Slider(0.0f,1.0f,0.1f,false,sliderStyle);
        tbl.add(sldSound).width(sldSound.getWidth()*width/1280).padTop(20*width/1280);
        tbl.row();
        //添加music标签 音乐滑动控件
        Label musicLbl = new Label("music",new Label.LabelStyle(font,font.getColor()));
        tbl.add(musicLbl).padTop(20*width/1280);
        musicLbl.setFontScale(1.3f*width/1280);
        sldMusic = new Slider(0.0f,1.0f,0.1f,false,sliderStyle);
        tbl.add(sldMusic).width(sldMusic.getWidth()*width/1280).padTop(20*width/1280);
        tbl.row();
        return tbl;
    }
    private Table buildOptWinNameSettings(){
//        BitmapFont font =new BitmapFont(Gdx.files.internal("menuscreen/winOptions.fnt"),Gdx.files.internal("menuscreen/winOptions.png"),false);
        DataController prefs = DataController.instance;
        Table tbl = new Table();
        font.getData().setScale(1.5f*width/1280f);
        tbl.pad(10,10,0,10);
        Label lbl = new Label("change your name",new Label.LabelStyle(font, Color.WHITE));
       // lbl.setFontScale(1.5f*width/1280);
        tbl.add(lbl).padTop(20*width/1280);
        tbl.row();

//        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(font, Color.WHITE, new TextureRegionDrawable(new Texture(Gdx.files.internal("menuscreen/textfieldcusor.png"))),
//                new TextureRegionDrawable(new Texture(Gdx.files.internal("menuscreen/textfieldbackground.png"))),
//                new TextureRegionDrawable(new Texture(Gdx.files.internal("menuscreen/textfieldbackground.png"))));
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle(font, Color.WHITE, new TextureRegionDrawable(AssetsController.instance.getRegion("textfieldcusor")),
                new TextureRegionDrawable(AssetsController.instance.getRegion("textfieldbackground")),
                new TextureRegionDrawable(AssetsController.instance.getRegion("textfieldbackground")));

        tfName = new TextField(prefs.getName(),textFieldStyle);
        tfName.setAlignment(Align.center);
        tbl.add(tfName).width(tfName.getPrefWidth()*width/1280f).height(tfName.getPrefHeight()*width/1280f).padTop(20*width/1280);
        return tbl;

    }
    private Table buildOptWinButtons(){
        Table tbl = new Table();

        //添加分割线
        Texture texture = new Texture(Gdx.files.internal("menuscreen/savecancelbutton.png"));
        //TextureRegion texture = AssetsController.instance.getRegion("savecancelbutton");
        font.getData().setScale(width/1280);
        //添加save按钮并且 初始化事件处理器
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(texture);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(textureRegionDrawable,textureRegionDrawable,textureRegionDrawable,font
        );


        btnWinOptSave = new TextButton("Save",textButtonStyle);
        tbl.add(btnWinOptSave).width(btnWinOptSave.getWidth()*width/1280f).height(btnWinOptSave.getHeight()*width/1280).padLeft(20*width/1280).padTop(20*width/1280);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
                AudioController.instance.play(AssetsController.instance.btnClicked);
            }
        });
        // 添加cancel按钮并且 初始化事件处理器
        btnWinOptCancel = new TextButton("Cancel", textButtonStyle);
        tbl.add(btnWinOptCancel).width(btnWinOptSave.getWidth()*width/1280f).height(btnWinOptSave.getHeight()*width/1280).padLeft(20*width/1280).padTop(20*width/1280);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCancelClicked();
                AudioController.instance.play(AssetsController.instance.btnClicked);
            }
        });
        return tbl;

    }






}