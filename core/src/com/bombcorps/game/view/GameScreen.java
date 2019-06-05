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
import com.badlogic.gdx.utils.viewport.Viewport;
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
    private WorldRenderer worldRenderer;
    private SpriteBatch batch;
    private boolean paused;

    private String[] description = {
            "被动技能：具备吸血30%能力\n" +
                    "技能一：消耗100血量+50精力，提高攻击力\n" +
                    "技能二：消耗80精力+30怒气，下次攻击\n" +
                    "获得额外30%暴击几率加成\n" +
                    "技能三：消耗100精力，和100怒气，\n" +
                    "立即获得300的血量补给，\n" +
                    "接下来3回合每回合回100血\n",
            "被动技能：远高于平均的血量与护甲，\n" +
                    "但牺牲攻击力\n" +
                    "技能一：消耗10怒气+50精力，增加护甲\n" +
                    "技能二：消耗80精力+30怒气，发射一枚\n" +
                    "护盾弹，为目标增加100点护甲\n" +
                    "技能三：消耗100精力+100怒气，两回合\n" +
                    "内获得60%减伤效果，且分担友\n" +
                    "方受到伤害的40%",
            "被动技能：牺牲护甲与攻击力，获得每回\n" +
                    "合固定回血的能力\n" +
                    "技能一：发射一枚恢复弹，为目标治疗\n" +
                    "10%最大生命值\n" +
                    "技能二：消耗80精力+30怒气,发射一枚虚\n" +
                    "弱弹，减少其攻击力、精力值\n" +
                    "技能三：消耗100精力+100怒气，友方集\n" +
                    "体回500血，接下来3回合友方\n" +
                    "增加10%伤害",
            "被动技能：具有高攻击力，且每次攻击成\n" +
                    "攻击成功暴击几率减半，失败\n" +
                    "暴击几率加倍\n" +
                    "技能一：消耗50精力+10怒气获得30%穿甲\n" +
                    "技能二：消耗80精力+30怒气，获得额外\n" +
                    "一次攻击机会，精力和怒气减半\n" +
                    "技能三：消耗100精力+100怒气，接下来\n" +
                    "两回合暴击几率不减少，且暴\n" +
                    "击伤害变为300%",
            "被动技能：无法造成物理伤害，但对目标\n" +
                    "积累一层中毒效果，目标每回\n" +
                    "合根据层数扣除血量\n" +
                    "技能一：下次攻击附加3层中毒效果\n" +
                    "技能二：消耗80精力+30怒气，攻击禁锢\n" +
                    "目标1回合\n" +
                    "技能三：消耗100精力+100怒气，消耗所\n" +
                    "有敌方的标记层数，对受到标记的单\n" +
                    "位造成10%最大生命值*层数的伤害"

    };

    public GameScreen(DirectedGame game,WorldController worldController){
        super(game);
        this.worldController = worldController;
        worldRenderer = new WorldRenderer(worldController);
        batch = new SpriteBatch();
        stage = new Stage();
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0x64/255.0f,0x95/255.0f,0xed/255.0f,0xff/255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldRenderer.render(batch);
        stage.act();
        stage.draw();

        if(worldController.isGameOver()==0){
            GameOver();
        }
    }

    public InputProcessor getInputProcessor(){
        return stage;
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
        stack.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage.addActor(stack);
        stack.add(layerBottom);
        stack.add(layerSkill);
        stack.add(layerHeroBasicInfoLayer);
        stack.add(layerHeroInfoWindow);
        stack.add(layerOtherHeroInfoWindow);
        stack.add(layerErrorQuitWindow);

    }

    public Table buildBottonLayer(){
        Table layer = new Table();
        //+ quit botton
        btnQuit = new Image(AssetsController.instance.getRegion("button_quit"));
        btnQuit.setSize(32,32);
        btnQuit.setPosition(0,Gdx.graphics.getHeight()-btnQuit.getHeight());
        layer.addActor(btnQuit);
        btnQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onQuitClicked();
                return true;
            }
        });
        btnSettings = new Image(AssetsController.instance.getRegion("button_setting"));
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
        imgSkillOne = new Image(AssetsController.instance.getRegion("SkillOne"));
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
        imgSkillTwo = new Image(AssetsController.instance.getRegion("SkillTwo"));
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
        imgSkillThree = new Image(AssetsController.instance.getRegion("SkillThree"));
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
        imgMove = new Image(AssetsController.instance.getRegion("move"));
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
        imgAttrack = new Image(AssetsController.instance.getRegion("attrack"));
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
        imgEjection = new Image(AssetsController.instance.getRegion("ejection"));
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
        imgTurnEnd = new Image(AssetsController.instance.getRegion("button_quit"));
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
        BitmapFont font = AssetsController.instance.font;
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        //+my hero's head appear
        imgMyHeroHead = new Image(AssetsController.instance.getRegion(myHeroType()+"_move0"));
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
        labelMyHeroBasicInfo = new Label("HP" +myPlayer().getMyHero().getHealth()+" AK:"+myPlayer().getMyHero().getAttack()+
        "\nED:"+myPlayer().getMyHero().getEndurance()+" AM:"+myPlayer().getMyHero().getArmor()+
         "\nRP:"+myPlayer().getMyHero().getRagePower()+" CP:"+myPlayer().getMyHero().getCriticalProbability(),labelStyle);
        labelMyHeroBasicInfo.setFontScale(width/15/labelMyHeroBasicInfo.getPrefWidth()*2);
        labelMyHeroBasicInfo.setPosition(width/15,0);
        layer.addActor(labelMyHeroBasicInfo);


        labelOtherHeroBasicInfo = new Label("HP:1000 AK:90\nED:100 AM:100\nPR:50 CP:20% ",labelStyle);
        labelOtherHeroBasicInfo.setFontScale(width/15/labelOtherHeroBasicInfo.getPrefWidth()*2);
        labelOtherHeroBasicInfo.setPosition(width-labelOtherHeroBasicInfo.getPrefWidth(),-labelOtherHeroBasicInfo.getHeight()/5);
        labelOtherHeroBasicInfo.setVisible(false);
        layer.addActor(labelOtherHeroBasicInfo);

        imgOtherHeroHead = new Image(AssetsController.instance.getRegion("Angel_move0"));
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
        BitmapFont font = AssetsController.instance.font;
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(AssetsController.instance.getRegion("textfieldbackground")));
        winHeroInfo = new Window("",windowStyle);
        winHeroInfo.setSize(height/2,height/2);
        //winOptions.setColor(1,1,1,1f);
        winHeroInfo.setVisible(false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.BLACK);
        Label label = new Label(description[myHeroTypeI()],labelStyle);
        label.setFontScaleX(18*winHeroInfo.getWidth()/label.getPrefWidth()/20);
        label.setFontScaleY(8*winHeroInfo.getHeight()/label.getPrefHeight()/9);
        label.setPosition(winHeroInfo.getWidth()/2-label.getPrefWidth()/2,0);
        winHeroInfo.addActor(label);
        //winOptions.pack();
        winHeroInfo.setPosition(0,imgMyHeroHead.getHeight());
        layer.addActor(winHeroInfo);
        return layer;
    }


    public Table buildOtherHeroInfoWindowLayer(){
        Table layer = new Table();
        int heroInfoType;
        heroInfoType = this.heroInfoType;
        BitmapFont font = AssetsController.instance.font;
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(AssetsController.instance.getRegion("textfieldbackground")));
        winOtherHeroInfo = new Window("",windowStyle);
        winOtherHeroInfo.setSize(height/2,height/2);
        //winOptions.setColor(1,1,1,1f);
        //winOtherHeroInfo.addActor(buildWinOHInfoQuitBotton());
        winOtherHeroInfo.setVisible(false);
        //winOptions.pack();
        winOtherHeroInfo.setPosition(width-winOtherHeroInfo.getWidth(),imgMyHeroHead.getHeight());
        layer.addActor(winOtherHeroInfo);
        return layer;
    }


    public Table buildErrorQuitWindowBotton(){
        Table layer = new Table();
        btnWinErrorQuit = new Image(AssetsController.instance.getRegion("confirm"));
        btnWinErrorQuit.setPosition(winErrorQuit.getWidth()/2-btnWinErrorQuit.getWidth()/2,winErrorQuit.getHeight()/3-btnWinErrorQuit.getHeight()/2);
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
        BitmapFont font = AssetsController.instance.font;
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(AssetsController.instance.getRegion("winresult")));
        winErrorQuit = new Window("",windowStyle);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.BLACK);
        Label label = new Label("Because Manager is quitted, game error stop.",labelStyle);
        winErrorQuit.setSize(width/2,height/2);
        winErrorQuit.setPosition(width/4,height/4);
        label.setPosition(winErrorQuit.getWidth()/2-label.getPrefWidth()/2,2*winErrorQuit.getHeight()/3-label.getPrefHeight()/2);
        //winOptions.setColor(1,1,1,1f);
        winErrorQuit.addActor(buildErrorQuitWindowBotton());
        winErrorQuit.addActor(label);
        winErrorQuit.setVisible(false);
        tbl.addActor(winErrorQuit);
        //winOptions.pack();
        return tbl;
    }

    public void onOtherHeroHeadClicked() {
        if(winOtherHeroInfo.isVisible()){
            winOtherHeroInfo.setVisible(false);
        }else{
            winOtherHeroInfo.setVisible(true);
        }
    }

    public void onHeroHeadClicked() {
        if(winHeroInfo.isVisible()){
            winHeroInfo.setVisible(false);
        }else{
            winHeroInfo.setVisible(true);
        }
    }

    public String myHeroType(){
        int heroType =  myHeroTypeI();
        if(heroType==0){
            return "Sparda";
        }
        else if(heroType==1){
            return "Protector";
        }
        else if(heroType==2){
            return  "Angel";
        }
        else if(heroType==3){
            return "Sniper";
        }
        else {
            return "Wizard";
        }
    }



    public Player myPlayer(){
        int i;
        for(i=0;i<worldController.getPlayers().size;i++) {
            if (worldController.getPlayers().get(i).getState() == Constants.PLAYER.STATE_LOCAL) {
                return worldController.getPlayers().get(i);

            }
        }
        return worldController.getPlayers().get(0);
    }

    public int myHeroTypeI(){
        return myPlayer().getHeroType();
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
        BitmapFont font = AssetsController.instance.font;
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        labelOtherHeroBasicInfo = new Label("HP:"+p.getMyHero().getHealth()+" AK:"+p.getMyHero().getAttack()+
                "\nED:"+p.getMyHero().getEndurance()+" AM:"+p.getMyHero().getArmor()+
                "\nRP:"+p.getMyHero().getRagePower()+" CP:"+p.getMyHero().getCriticalProbability(),labelStyle);
        labelOtherHeroBasicInfo.setFontScale(width/15/labelOtherHeroBasicInfo.getPrefWidth()*2);
        labelOtherHeroBasicInfo.setPosition(width/15*13,-labelOtherHeroBasicInfo.getHeight()/5);


        imgOtherHeroHead = new Image(AssetsController.instance.getRegion(i+"_move0"));
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
        Label label = new Label(description[heroType],labelStyle);
        label.setFontScaleX(18*winOtherHeroInfo.getWidth()/label.getPrefWidth()/20);
        label.setFontScaleY(8*winOtherHeroInfo.getHeight()/label.getPrefHeight()/9);
        label.setPosition(winOtherHeroInfo.getWidth()/2-label.getPrefWidth()/2,0);
        winOtherHeroInfo.addActor(label);
        imgOtherHeroHead.setVisible(true);
        labelOtherHeroBasicInfo.setVisible(true);
    }

    public void onManagerQuitClicked() {
        //game.loadScreen
    }

    public void onQuitClicked() {
        game.loadMenuScreen();
    }

    public void GameOver(){
        //游戏结算的弹窗
        BitmapFont font = AssetsController.instance.font;
        TextureRegionDrawable winResultsDrawable = new TextureRegionDrawable(AssetsController.instance.getRegion("winresult"));
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),winResultsDrawable);
        winResults = new Window("",windowStyle);
        winResults.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        winResults.setPosition((Gdx.graphics.getWidth()-winResults.getWidth())/2,(Gdx.graphics.getHeight()-winResults.getHeight())/2);



        if(worldController.isGameOver()==1){
            virtory = new Image(AssetsController.instance.getRegion("vitory"));
            virtory.setSize(winResults.getWidth()/3,winResults.getHeight()/3);
            virtory.setPosition((winResults.getWidth()-virtory.getWidth())/2,(winResults.getHeight()-virtory.getHeight())/1.25f);
            winResults.addActor(virtory);
        }else if(worldController.isGameOver()==2){
            failed = new Image(AssetsController.instance.getRegion("failed"));
            failed.setSize(winResults.getWidth()/3,winResults.getHeight()/3);
            failed.setPosition((winResults.getWidth()-virtory.getWidth())/2,(winResults.getHeight()-virtory.getHeight())/1.25f);
            winResults.addActor(failed);
        }



        Label goldReceiveLabel = new Label("获得1000金币",new Label.LabelStyle(font,Color.BLACK));
        winResults.addActor(goldReceiveLabel);
        goldReceiveLabel.setSize(winResults.getWidth(),winResults.getHeight()/2);
        goldReceiveLabel.setAlignment(Align.center);
        goldReceiveLabel.setPosition(0,winResults.getHeight()/5);
        Image confirm = new Image(AssetsController.instance.getRegion("confirm"));
        confirm.setSize(winResults.getWidth()/3,winResults.getHeight()/3);
        confirm.setPosition((winResults.getWidth()-virtory.getWidth())/2,50);
        winResults.addActor(confirm);
        confirm.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //game.loadRoomScreen();
                winResults.setVisible(false);
                return true;

            }
        });


    }

    public void playQuit(String ID){
        BitmapFont font = AssetsController.instance.font;
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label label = new Label(ID+" has already quitted the game ,nmsl",labelStyle);
        label.setPosition(0,height/2-label.getPrefHeight()/2);
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
        BitmapFont font = AssetsController.instance.font;
        TextureRegionDrawable winResultsDrawable = new TextureRegionDrawable(AssetsController.instance.getRegion("window"));
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),winResultsDrawable);
        winInGameSettings = new Window("Settings",windowStyle);
        Table tbl = new Table();
        //添加标题audio
        tbl.pad(10,10,0,10);
        Label audioLbl = new Label("Audio",new Label.LabelStyle(font,font.getColor()));
        audioLbl.setFontScale(1.5f*width/1280f);
        tbl.add(audioLbl).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        //添加sound标签 声音滑动控件
        Label soundLbl = new Label("Sound",new Label.LabelStyle(font,font.getColor()));
        soundLbl.setFontScale(1.5f*width/1280f);
        tbl.add(soundLbl);
        TextureRegion sliderBac= AssetsController.instance.getRegion("sliderbackground");
        Image image = new Image(sliderBac);
        //TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(sliderBac);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(new TextureRegionDrawable(sliderBac),
                new TextureRegionDrawable(AssetsController.instance.getRegion("sliderkuai")));
        sldSound = new Slider(0.0f,1.0f,0.1f,false,sliderStyle);
        tbl.add(sldSound).width(sldSound.getWidth()*width/1280);
        tbl.row();
        //添加music标签 音乐滑动控件
        Label musicLbl = new Label("Music",new Label.LabelStyle(font,font.getColor()));
        tbl.add(musicLbl);
        musicLbl.setFontScale(1.5f*width/1280);
        sldMusic = new Slider(0.0f,1.0f,0.1f,false,sliderStyle);
        tbl.add(sldMusic).width(sldMusic.getWidth()*width/1280);
        tbl.row();





        //添加save按钮并且 初始化事件处理器
        font.getData().setScale(width/1280);
        //添加save按钮并且 初始化事件处理器
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(AssetsController.instance.getRegion("savecancelbutton"));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(textureRegionDrawable,textureRegionDrawable,textureRegionDrawable,font
        );
        btnWinOptSave = new TextButton("Save",textButtonStyle);
        tbl.add(btnWinOptSave).width(btnWinOptSave.getWidth()*width/1280f).height(btnWinOptSave.getHeight()*width/1280).padLeft(20*width/1280);
        btnWinOptSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onSaveClicked();
            }
        });
        // 添加cancel按钮并且 初始化事件处理器
        btnWinOptCancel = new TextButton("Cancel", textButtonStyle);
        tbl.add(btnWinOptCancel).width(btnWinOptSave.getWidth()*width/1280f).height(btnWinOptSave.getHeight()*width/1280).padLeft(20*width/1280);
        btnWinOptCancel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onCancelClicked();
            }
        });
        winInGameSettings.addActor(tbl);
        winInGameSettings.setSize(width/2,height/2);
        winInGameSettings.setPosition(width/4,height/4);
        winInGameSettings.setVisible(true);
        stage.addActor(winInGameSettings);
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