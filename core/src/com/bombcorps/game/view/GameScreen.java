package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.DataController;
import com.bombcorps.game.controller.WorldController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Player;


/*
图片路径均非真正设置
 */
public class GameScreen extends AbstractGameScreen{
    private static final String TAG = GameScreen.class.getName();
    private final float width = Gdx.graphics.getWidth();
    private final float height = Gdx.graphics.getHeight();
    private WorldController worldController;
    private SpriteBatch batch;
    private boolean paused;

    public GameScreen(DirectedGame game,WorldController worldController){
        super(game);
        this.worldController = worldController;
        batch = new SpriteBatch();
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0x64/255.0f,0x95/255.0f,0xed/255.0f,0xff/255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldController.getWorld().render(batch);
        stage.act();
        stage.draw();

        if(worldController.isGameOver()==0){
            GameOver();
        }
    }

    public InputProcessor getInputProcessor(){
        return (InputProcessor) worldController;
    }
    private Stage stage;



    //英雄详细信息弹窗
    private int heroInfoType;
    private Image imgMyHeroHead;
    private Image imgOtherHeroHead;
    private Label labelMyHeroBasicInfo;
    private Label labelOtherHeroBasicInfo;
    private Window winHeroInfo;
    private Window winOtherHeroInfo;
    private Image btnwinHInfoQuit;
    private Image btnwinOHInfoQuit;
    //游戏异常退出弹窗
    private Window winErrorQuit;
    private Image btnWinErrorQuit;
    //
    private Image imgSkillOne;
    private Image imgSkillTwo;
    private Image imgSkillThree;
    private Image imgMove;
    private Image imgAttrack;
    private Image imgEjection;
    private Image imgTurnEnd;
    //
    private Image btnQuit;
    private Image btnSettings;
    private Image virtory;
    private Image failed;

    private Window winInGameSettings;
    private Window winResults;
    private Slider sldSound;
    private Slider sldMusic;
    private TextButton btnWinOptSave;
    private TextButton btnWinOptCancel;


    public void rebuildStage(){
        //build all layers
        Table layerBottom = buildBottonLayer();
        Table layerSkill = buildSkillLayer();
        Table layerHeroBasicInfoLayer = buildHeroBasicInfoLayer();
        Table layerHeroInfoWindow = buildHeroInfoWindowLayer();
        Table layerOtherHeroInfoWindow = buildOtherHeroInfoWindowLayer();
        Table layerErrorQuitWindow = buildErrorQuitWindowLayer();

        //assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerBottom);
        stack.add(layerSkill);
        stack.add(layerHeroBasicInfoLayer);
        stack.add(layerHeroInfoWindow);
        stack.add(layerOtherHeroInfoWindow);
        stack.add(layerErrorQuitWindow);

        stage.addActor(winInGameSettings);

        winInGameSettings.setVisible(false);

    }

    public Table buildBottonLayer(){
        Table layer = new Table();
        //+ quit botton
        btnQuit = new Image(new Texture(Gdx.files.internal("images/button_quit.png")));
        btnQuit.setSize(32,32);
        btnQuit.setPosition(0,height-btnQuit.getHeight());
        layer.addActor(btnQuit);
        btnQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onQuitClicked();
                return true;
            }
        });
        btnSettings = new Image(new Texture(Gdx.files.internal("images/button_setting.png")));
        btnSettings.setSize(32,32);
        btnSettings.setPosition(width-btnSettings.getWidth(),height-btnSettings.getHeight());
        layer.addActor(btnSettings);
        btnSettings.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击设置
                onWinInGameSettingsClicked();
                return true;
            }
        });
        return layer;
    }

    public Table buildSkillLayer(){
        Table layer = new Table();
        float scale;
        //+SkillOneLayer
        imgSkillOne = new Image(new Texture(Gdx.files.internal("skill/SkillOne.png")));
        scale=width/15/imgSkillOne.getWidth();
        imgSkillOne.setScale(scale);
        imgSkillOne.setPosition(width/2-imgSkillOne.getWidth()*scale*3.5f,0);
        layer.addActor(imgSkillOne);
        imgSkillOne.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击技能一
                worldController.onOperationClicked(3);
                return true;
            }
        });

        //+SkillTwoLayer
        imgSkillTwo = new Image(new Texture(Gdx.files.internal("skill/SkillTwo.png")));
        imgSkillTwo.setScale(scale);
        imgSkillTwo.setPosition(imgSkillOne.getX()+imgSkillOne.getWidth()*scale,0);
        layer.addActor(imgSkillTwo);
        imgSkillTwo.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击技能二
                worldController.onOperationClicked(4);
                return true;
            }
        });

        //+SkillThreeLayer
        imgSkillThree = new Image(new Texture(Gdx.files.internal("skill/SkillThree.png")));
        imgSkillThree.setScale(scale);
        imgSkillThree.setPosition(imgSkillTwo.getX()+imgSkillTwo.getWidth()*scale,0);
        layer.addActor(imgSkillThree);
        imgSkillThree.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击技能三
                worldController.onOperationClicked(5);
                return true;
            }
        });

        //+MovementLayer
        imgMove = new Image(new Texture(Gdx.files.internal("skill/move.png")));
        imgMove.setScale(scale);
        imgMove.setPosition(imgSkillThree.getX()+imgSkillThree.getWidth()*scale,0);
        layer.addActor(imgMove);
        imgMove.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击移动
                worldController.onOperationClicked(0);
                return true;
            }
        });

        //+AttrackLayer
        imgAttrack = new Image(new Texture(Gdx.files.internal("skill/attrack.png")));
        imgAttrack.setScale(scale);
        imgAttrack.setPosition(imgMove.getX()+imgMove.getWidth()*scale,0);
        layer.addActor(imgAttrack);
        imgAttrack.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击发射炸弹
                worldController.onOperationClicked(2);
                return true;
            }
        });

        //+EjectionLayer
        imgEjection = new Image(new Texture(Gdx.files.internal("skill/ejection.png")));
        imgEjection.setScale(scale);
        imgEjection.setPosition(imgAttrack.getX()+imgAttrack.getWidth()*scale,0);
        layer.addActor(imgEjection);
        imgEjection.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击弹射
                worldController.onOperationClicked(1);
                return true;
            }
        });
        //+TurnEndLayer
        imgTurnEnd = new Image(new Texture(Gdx.files.internal("Skill/quit.png")));
        imgTurnEnd.setSize(43,35);
        imgTurnEnd.setScale(scale);
        imgTurnEnd.setPosition(imgEjection.getX()+imgEjection.getWidth()*scale,0);
        layer.addActor(imgTurnEnd);
        imgTurnEnd.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击回合结束
                worldController.onOperationClicked(0);
                return true;
            }
        });




        return layer;
    }

    public Table buildHeroBasicInfoLayer(){

        Table layer =new Table();
        BitmapFont font = new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        TextureRegion textureRegion = new TextureRegion();
        textureRegion = AssetsController.instance.getRegion(myHeroType()+"_stand");
        //+my hero's head appear
        imgMyHeroHead = new Image(textureRegion.getTexture());
        imgMyHeroHead.setPosition(0,0);
        imgMyHeroHead.setScale(width/15/imgMyHeroHead.getWidth());
        layer.addActor(imgMyHeroHead);
        imgMyHeroHead.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击自己英雄头像显示详细信息
                onHeroHeadClicked();
                return true;
            }
        });
        Player p =new Player(myHeroType());
        labelMyHeroBasicInfo = new Label("HP:"+p.getMyHero().getHealth()+" AK:"+p.getMyHero().getAttack()+
                "\nED:"+p.getMyHero().getEndurance()+" AM:"+p.getMyHero().getArmor()+
                "\nRP:"+p.getMyHero().getRagePower()+" CP:"+p.getMyHero().getCriticalProbability() ,labelStyle);
        labelMyHeroBasicInfo.setFontScale(width/15/labelMyHeroBasicInfo.getPrefWidth()*2);
        labelMyHeroBasicInfo.setPosition(width/15,-labelMyHeroBasicInfo.getHeight()/5);
        layer.addActor(labelMyHeroBasicInfo);


        labelOtherHeroBasicInfo = new Label("HP:1000 AK:90\nED:100 AM:100\nPR:50 CP:20% ",labelStyle);
        labelOtherHeroBasicInfo.setFontScale(width/15/labelOtherHeroBasicInfo.getPrefWidth()*2);
        labelOtherHeroBasicInfo.setPosition(width/15*13,-labelOtherHeroBasicInfo.getHeight()/5);
        labelOtherHeroBasicInfo.setVisible(false);
        layer.addActor(labelOtherHeroBasicInfo);

        TextureRegion textureRegion1 = new TextureRegion();
        textureRegion1 = AssetsController.instance.getRegion("Sparda_stand");
        imgOtherHeroHead = new Image(textureRegion1.getTexture());
        imgOtherHeroHead.setScale(width/15/imgOtherHeroHead.getWidth());
        imgOtherHeroHead.setPosition(width/15*12,0);
        imgOtherHeroHead.setVisible(false);
        layer.addActor(imgOtherHeroHead);
        imgOtherHeroHead.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击他人英雄头像显示详细信息
                onOtherHeroHeadClicked();
                return true;
            }
        });

        return layer;
    }

    public Table buildHeroInfoWindowLayer(){
        Table layer = new Table();
        Image background = new Image(new Texture(Gdx.files.internal("Skill/skillDetail/"+myHeroType()+"_skill_detail")));
        background.setPosition(0,0);
        background.setSize(height/2,height/2);
        BitmapFont font =new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("winresult.png"))));
        winHeroInfo = new Window("",windowStyle);
        winHeroInfo.setSize(height/2,height/2);
        //winOptions.setColor(1,1,1,1f);
        winHeroInfo.addActor(buildWinHInfoQuitBotton());
        winHeroInfo.setVisible(false);

        //winOptions.pack();
        winHeroInfo.setPosition(0,imgMyHeroHead.getHeight());
        layer.addActor(winHeroInfo);
        return layer;
    }

    public Table buildWinHInfoQuitBotton(){
        Table tbl = new Table();
        btnwinHInfoQuit = new Image(new Texture(Gdx.files.internal("skill/quit.png")));
        btnwinHInfoQuit.setPosition(winHeroInfo.getWidth()-btnwinHInfoQuit.getWidth(),winHeroInfo.getHeight()-btnwinHInfoQuit.getHeight());
        tbl.addActor(btnwinHInfoQuit);
        btnwinHInfoQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击自己英雄头像显示详细信息
                onWinHInfoQuitBottonClicked();
                return true;
            }
        });



        return tbl;
    }

    public Table buildOtherHeroInfoWindowLayer(){
        Table layer = new Table();
        int heroInfoType;
        heroInfoType = this.heroInfoType;
        BitmapFont font =new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("images/winresult.png"))));
        winOtherHeroInfo = new Window("",windowStyle);
        winOtherHeroInfo.setSize(height/2,height/2);
        //winOptions.setColor(1,1,1,1f);
        winOtherHeroInfo.addActor(buildWinOHInfoQuitBotton());
        winOtherHeroInfo.setVisible(false);

        //winOptions.pack();
        winOtherHeroInfo.setPosition(width-winOtherHeroInfo.getWidth(),imgMyHeroHead.getHeight());
        layer.addActor(winOtherHeroInfo);
        return layer;
    }

    public Table buildWinOHInfoQuitBotton(){
        Table tbl = new Table();
        btnwinOHInfoQuit = new Image(new Texture(Gdx.files.internal("skill/quit.png")));
        btnwinOHInfoQuit.setPosition(winOtherHeroInfo.getWidth()-btnwinOHInfoQuit.getWidth(),winOtherHeroInfo.getHeight()-btnwinOHInfoQuit.getHeight());
        tbl.addActor(btnwinOHInfoQuit);
        btnwinOHInfoQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击他人英雄头像显示详细信息
                onWinOHInfoQuitBottonClicked();
                return true;
            }
        });



        return tbl;
    }

    public Table buildErrorQuitWindowBotton(){
        Table layer = new Table();
        btnWinErrorQuit = new Image(new Texture(Gdx.files.internal("images/button_quit.png")));
        btnWinErrorQuit.setPosition(winErrorQuit.getWidth()-btnWinErrorQuit.getWidth(),winErrorQuit.getHeight()-btnWinErrorQuit.getHeight());
        layer.addActor(btnWinErrorQuit);
        btnWinErrorQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击确认
                onErrorQuitBottonClicked();
                return true;
            }
        });
        return layer;
    }

    public Table buildErrorQuitWindowLayer(){
        Table tbl = new Table();
        BitmapFont font =new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("images/winresult.png"))));
        winErrorQuit = new Window("",windowStyle);
        winErrorQuit.setSize(width/2,height/2);
        winErrorQuit.setPosition(width/4,height/4);
        //winOptions.setColor(1,1,1,1f);
        winErrorQuit.addActor(buildErrorQuitWindowBotton());
        winErrorQuit.setVisible(false);
        tbl.addActor(winErrorQuit);
        //winOptions.pack();
        return tbl;
    }



    public void onWinHInfoQuitBottonClicked() {
        winHeroInfo.setVisible(false);
    }

    public void onWinOHInfoQuitBottonClicked() {
        winOtherHeroInfo.setVisible(false);
    }

    public void onOtherHeroHeadClicked() {
        winOtherHeroInfo.setVisible(true);
    }

    public void onHeroHeadClicked() {
        winHeroInfo.setVisible(true);
    }

    public String myHeroType(){
        int i;
        for(i=0;worldController.getPlayers().get(i).getState() != Constants.PLAYER.STATE_LOCAL;i++){

        }
        i = worldController.getPlayers().get(i).getHeroType();
        if(i==0){
            return "Sparda";
        }
        else if(i==1){
            return "Protector";
        }
        else if(i==2){
            return  "Angel";
        }
        else if(i==3){
            return "Sniper";
        }
        else {
            return "Wizard";
        }
    }

    public void onHeroClicked(Player p){
        final int heroType;
        heroType = p.getHeroType();
        String i;
        if(heroType == 0){
            i = "Sparda";
        }else if(heroType == 1){
            i = "Protector";
        }else if(heroType == 2){
            i = "Angel";
        }else if(heroType == 3){
            i = "Sniper";
        }else{
            i = "Wizard";
        }
        BitmapFont font = new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        labelOtherHeroBasicInfo = new Label("HP:"+p.getMyHero().getHealth()+" AK:"+p.getMyHero().getAttack()+
                "\nED:"+p.getMyHero().getEndurance()+" AM:"+p.getMyHero().getArmor()+
                "\nRP:"+p.getMyHero().getRagePower()+" CP:"+p.getMyHero().getCriticalProbability(),labelStyle);
        labelOtherHeroBasicInfo.setFontScale(width/15/labelOtherHeroBasicInfo.getPrefWidth()*2);
        labelOtherHeroBasicInfo.setPosition(width/15*13,-labelOtherHeroBasicInfo.getHeight()/5);

        TextureRegion textureRegion1 = new TextureRegion();
        textureRegion1 = AssetsController.instance.getRegion(i+"_stand");

        imgOtherHeroHead = new Image(textureRegion1.getTexture());
        imgOtherHeroHead.setScale(width/15/imgOtherHeroHead.getWidth());
        imgOtherHeroHead.setPosition(width/15*12,0);
        imgOtherHeroHead.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击他人英雄头像显示详细信息
                onOtherHeroHeadClicked();
                return true;
            }
        });
        Image background = new Image(new Texture(Gdx.files.internal("Skill/skillDetail"+i+"_skill_detail")));
        background.setPosition(0,0);
        background.setSize(height/2,height/2);
        winOtherHeroInfo.addActor(background);
        imgOtherHeroHead.setVisible(true);
        labelOtherHeroBasicInfo.setVisible(true);
    }

    public void onManagerQuitClicked() {
        //game.loadScreen
    }

    public void onQuitClicked() {
        game.loadLobbyScreen();
    }

    public void GameOver(){
        //游戏结算的弹窗
        BitmapFont font =new BitmapFont(Gdx.files.internal("images/winOptions.fnt"),Gdx.files.internal("images/winOptions.png"),false);
        TextureRegionDrawable winResultsDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("images/winresult.png")));
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),winResultsDrawable);
        winResults = new Window("",windowStyle);
        winResults.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        winResults.setPosition((Gdx.graphics.getWidth()-winResults.getWidth())/2,(Gdx.graphics.getHeight()-winResults.getHeight())/2);



        if(worldController.isGameOver()==1){
            virtory = new Image(new Texture(Gdx.files.internal("images/vitory.png")));
            virtory.setSize(winResults.getWidth()/3,winResults.getHeight()/3);
            virtory.setPosition((winResults.getWidth()-virtory.getWidth())/2,(winResults.getHeight()-virtory.getHeight())/1.25f);
            winResults.addActor(virtory);
        }else if(worldController.isGameOver()==2){
            failed = new Image(new Texture(Gdx.files.internal("images/failed.png")));
            failed.setSize(winResults.getWidth()/3,winResults.getHeight()/3);
            failed.setPosition((winResults.getWidth()-virtory.getWidth())/2,(winResults.getHeight()-virtory.getHeight())/1.25f);
            winResults.addActor(failed);
        }



        Label goldReceiveLabel = new Label("获得1000金币",new Label.LabelStyle(font,Color.BLACK));
        winResults.addActor(goldReceiveLabel);
        goldReceiveLabel.setSize(winResults.getWidth(),winResults.getHeight()/2);
        goldReceiveLabel.setAlignment(Align.center);
        goldReceiveLabel.setPosition(0,winResults.getHeight()/5);
        Image confirm = new Image(new Texture("images/confirm.png"));
        confirm.setSize(winResults.getWidth()/3,winResults.getHeight()/3);
        confirm.setPosition((winResults.getWidth()-virtory.getWidth())/2,50);
        winResults.addActor(confirm);
        confirm.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.loadRoomScreen(worldController.getWorld().getLimit());
                winResults.setVisible(false);
                return true;

            }
        });


    }

    public void playQuit(String ID){
        BitmapFont font = new BitmapFont(Gdx.files.internal("winOptions.fnt"),Gdx.files.internal("winOptions.png"),false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label label = new Label(ID+" has already quitted the game ,nmsl",labelStyle);
        stage.addActor(label);

    }

    public void onErrorQuitBottonClicked() {
        winErrorQuit.setVisible(false);
        game.loadLobbyScreen();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(
                Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

    @Override
    public void hide() {
        batch.dispose();
        //worldController.dispose();
    }

    @Override
    public void pause() {
        paused = true;
    }

    public void resume(){
        super.resume();
        paused = false;
    }

    public void errorStop(){
        winErrorQuit.setVisible(true);
    }

public void onWinInGameSettingsClicked(){

    BitmapFont font =new BitmapFont(Gdx.files.internal("winOptions.fnt"),Gdx.files.internal("winOptions.png"),false);
    TextureRegionDrawable winResultsDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("winOptions.png")));
    Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),winResultsDrawable);
    winInGameSettings = new Window("Settings",windowStyle);
    Table tbl = new Table();
    //添加标题audio
    tbl.pad(10,10,0,10);
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
    winInGameSettings.addActor(tbl);
    winInGameSettings.setVisible(true);
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
}
    private void onSaveClicked() {
        saveSettings();
        onCancelClicked();
    }
    public void onCancelClicked(){
         winInGameSettings.setVisible(false);
    }
    public void saveSettings(){
        DataController prefs = DataController.instance;
        prefs.setVolSound(sldSound.getValue());
        prefs.setVolMusic(sldMusic.getValue());
        prefs.saveSettings();
    }

}
