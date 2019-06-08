package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.AudioController;
import com.bombcorps.game.controller.DataController;
import com.bombcorps.game.controller.InputController;
import com.bombcorps.game.controller.WorldController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.CurPlayerSignal;
import com.bombcorps.game.model.Player;


/*
图片路径均非真正设置
 */
public class GameScreen extends AbstractGameScreen{
    public final String TAG = GameScreen.class.getName();
    public final float width = Constants.VIEWPORT_GUI_WIDTH;
    public final float height = Constants.VIEWPORT_GUI_HEIGHT;
    private boolean paused = false;
    private boolean isquit;
    private boolean isPlayed;
    private boolean isClickedHero;

    private Player otherPlayer;
    private String quitPlayer;
    private CurPlayerSignal curPlayerSignal;

    private BitmapFont font;
    private float scale;
    public String[] description = {
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

    private Color c[] = {Color.GOLD, Color.BLUE, Color.RED};

    public OrthographicCamera camera;
    public OrthographicCamera cameraGUI;
    public SpriteBatch batch;
    public Stage stage;
    public WorldController worldController;
    public InputController inputController;

    //退出和设置按钮
    public Sprite btnQuit;
    public Sprite btnSettings;
    //技能图标
    public Sprite imgMove;
    public Sprite imgEjection;
    public Sprite imgAttrack;
    public Sprite imgSkillOne;
    public Sprite imgSkillTwo;
    public Sprite imgSkillThree;
    public Sprite imgTurnEnd;
    //英雄头像与基础信息
    public Sprite imgMyHeroHead;
    public Sprite imgOtherHeroHead;
    public Sprite bar;
    //设置窗口
    public Window winOptions;
    public Slider sldSound;
    public Slider sldMusic;
    public TextButton btnWinOptSave;
    public TextButton btnWinOptCancel;
    //英雄技能详细信息窗口
    public Window winHeroInfo;
    public Window winOtherHeroInfo;
    public Image btnwinHInfoQuit;
    public Image btnwinOHInfoQuit;
    //结果窗口
    public Window winResults;
    public Image virtory;
    public Image failed;
    //异常退出窗口

    public Window winErrorQuit;
    public Image btnWinErrorQuit;


    public GameScreen(DirectedGame game, WorldController worldController){
        super(game);
        this.worldController = worldController;
        inputController = new InputController(this, worldController);
        camera = worldController.getCamera();
        init();
    }

    public void init(){
        batch = new SpriteBatch();

        font = AssetsController.instance.font;

        isClickedHero = false;
        isquit = false;
        isPlayed = false;

        worldController.getCameraController().setPosition(Constants.VIEWPORT_WIDTH/2,Constants.VIEWPORT_HEIGHT/2);
        worldController.getCameraController().setTarget(worldController.getCurPlayer());
        camera.update();

        curPlayerSignal = new CurPlayerSignal();

        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(Constants.VIEWPORT_GUI_WIDTH/2,Constants.VIEWPORT_GUI_HEIGHT/2,0);
        //cameraGUI.setToOrtho(true);
        cameraGUI.update();


        //退出按钮
        btnQuit = new Sprite(AssetsController.instance.getRegion("mapleft"));
        btnQuit.setSize(0.045f * width,0.07f * height);
        btnQuit.setPosition(0,0.91f * height);
        //设置按钮
        btnSettings = new Sprite(AssetsController.instance.getRegion("button_setting"));
        btnSettings.setSize(0.045f * width,0.07f * height);
        btnSettings.setPosition(width-btnSettings.getWidth(),0.91f * height);
        //移动按钮
        imgMove = new Sprite(AssetsController.instance.getRegion("move")) ;
        scale=width/15/imgMove.getWidth();
        imgMove.setScale(scale);
        imgMove.setPosition(width/2-imgMove.getWidth()*scale*3.5f,0);
        //弹射按钮
        imgEjection = new Sprite(AssetsController.instance.getRegion("ejection")) ;
        imgEjection.setScale(scale);
        imgEjection.setPosition(imgMove.getX()+imgMove.getWidth()*scale,0);
        //发射炮弹按钮
        imgAttrack = new Sprite(AssetsController.instance.getRegion("attrack"));
        imgAttrack.setScale(scale);
        imgAttrack.setPosition(imgEjection.getX()+imgEjection.getWidth()*scale,0);
        //一技能按钮
        imgSkillOne = new Sprite(AssetsController.instance.getRegion("SkillOne"));
        imgSkillOne.setScale(scale);
        imgSkillOne.setPosition(imgAttrack.getX()+imgAttrack.getWidth()*scale,0);
        //二技能按钮
        imgSkillTwo = new Sprite(AssetsController.instance.getRegion("SkillTwo"));
        imgSkillTwo.setScale(scale);
        imgSkillTwo.setPosition(imgSkillOne.getX()+imgSkillOne.getWidth()*scale,0);
        //三技能按钮
        imgSkillThree = new Sprite(AssetsController.instance.getRegion("SkillThree"));
        imgSkillThree.setScale(scale);
        imgSkillThree.setPosition(imgSkillTwo.getX()+imgSkillTwo.getWidth()*scale,0);
        //回合结束按钮
        imgTurnEnd = new Sprite(AssetsController.instance.getRegion("button_quit"));
        imgTurnEnd.setSize(43,35);
        imgTurnEnd.setScale(scale);
        imgTurnEnd.setPosition(imgSkillThree.getX()+imgSkillThree.getWidth()*scale,0);
        //本人英雄头像
        imgMyHeroHead = new Sprite(AssetsController.instance.getRegion(myHeroType()+"_move0"));
        imgMyHeroHead.setSize(43,43);
        imgMyHeroHead.setScale(scale);
        imgMyHeroHead.setPosition(0,0);


        //他人英雄头像
        imgOtherHeroHead = new Sprite(AssetsController.instance.getRegion(myHeroType()+"_move0"));
        imgOtherHeroHead.setSize(43,43);
        imgOtherHeroHead.setScale(scale);
        imgOtherHeroHead.setPosition(width/15*12,0);

        //属性条
        bar = new Sprite(AssetsController.instance.getRegion("white"));
        bar.setSize(width/15*1.5f, 10f);
    }

    public void rebuildStage(){
        //build all layers
        Table layerHeroInfoWindow = buildHeroInfoWindowLayer();
        Table layerOtherHeroInfoWindow = buildOtherHeroInfoWindowLayer();
        Table layerErrorQuitWindow = buildErrorQuitWindowLayer();
        Table layerOptionsWindow = buildOptionsWindowLayer();
        //assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stack.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage.addActor(stack);
        stack.add(layerHeroInfoWindow);
        stack.add(layerOtherHeroInfoWindow);
        stack.add(layerErrorQuitWindow);
        stack.add(layerOptionsWindow);

    }

    public void renderWorld(SpriteBatch batch){
        worldController.getCameraController().applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        worldController.getWorld().render(batch);
        curPlayerSignal.setPosition(worldController.getCurPlayer().getPosition().x+0.4f,worldController.getCurPlayer().getPosition().y+1.05f);
        curPlayerSignal.render(batch);
    }

    public void renderGUI(SpriteBatch batch){
        batch.setProjectionMatrix(cameraGUI.combined);
        batchBotton(batch);
        if(myPlayer().equals(worldController.getCurPlayer())){
            batchSkill(batch);
        }
        batchHeroInfo(batch);
        if(isClickedHero){
            batchOtherHeroInfo(batch);
        }
        if(isquit){
            batchSbQuit(batch);
        }
    }

    public void batchBotton(SpriteBatch batch){
        btnQuit.draw(batch);
        btnSettings.draw(batch);
    }

    public void batchSkill(SpriteBatch batch){
        imgMove.draw(batch);
        imgEjection.draw(batch);
        imgAttrack.draw(batch);
        imgSkillOne.draw(batch);
        imgSkillTwo.draw(batch);
        imgSkillThree.draw(batch);
        imgTurnEnd.draw(batch);
    }

    public void batchHeroInfo(SpriteBatch batch){
        imgMyHeroHead.draw(batch);
        bar.setX(imgMyHeroHead.getX()+43*scale);
        for(int i=2;i>=0;i--) {
            bar.setColor(Color.GRAY);
            bar.setSize(width/15*1.5f,bar.getHeight());
            bar.setY(i*15+1);
            bar.draw(batch);

            bar.setColor(c[i]);
            bar.setY(i*15+1);
            if(i == 2){
                bar.setSize(width/15*1.5f*myPlayer().getMyHero().getHealth()/myPlayer().getMyHero().getMaxHealth(), bar.getHeight());
            }else if(i == 1){
                bar.setSize(width/15*1.5f*myPlayer().getMyHero().getEndurance()/Constants.MAX_ENDURENCE,bar.getHeight());
            }else if(i==0){
                bar.setSize(width/15*1.5f*myPlayer().getMyHero().getRagePower()/Constants.MAX_RAGEPOWER,bar.getHeight());
            }
            bar.draw(batch);
        }
    }

    public void batchOtherHeroInfo(SpriteBatch batch){
        imgOtherHeroHead.draw(batch);
        bar.setX(imgOtherHeroHead.getX()+43*scale);
        for(int i=2;i>=0;i--) {
            bar.setColor(Color.GRAY);
            bar.setSize(width/15*1.5f,bar.getHeight());
            bar.setY(i*15+1);
            bar.draw(batch);

            bar.setColor(c[i]);
            bar.setY(i*15+1);
            if(i == 2){
                bar.setSize(width/15*1.5f*otherPlayer.getMyHero().getHealth()/otherPlayer.getMyHero().getMaxHealth(), bar.getHeight());
            }else if(i == 1){
                bar.setSize(width/15*1.5f*otherPlayer.getMyHero().getEndurance()/Constants.MAX_ENDURENCE,bar.getHeight());

            }else {
                bar.setSize(width/15*1.5f*otherPlayer.getMyHero().getRagePower()/Constants.MAX_RAGEPOWER,bar.getHeight());
            }
            bar.draw(batch);
        }
    }


    public void batchSbQuit(SpriteBatch batch){
        font.getData().setScale(1.0f);
        font.setColor(Color.BLACK);
        font.draw(batch,quitPlayer+" is quit.",0,height/2);
    }


    //设置窗口
    public Table buildOptionsWindowLayer(){
        Table layer = new Table();
//        BitmapFont font =new BitmapFont(Gdx.files.internal("menuscreen/winOptions.fnt"), Gdx.files.internal("menuscreen/winOptions.png"),false)
//        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("menuscreen/window.png"))));
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(AssetsController.instance.getRegion("window")));
        winOptions = new Window("Options",windowStyle);
        winOptions.add(buildOptWinAudioSettings()).row();
        winOptions.add(buildOptWinButtons()).pad(5,0,10,0);
        winOptions.setColor(1,1,1,1f);
        winOptions.setVisible(false);
        winOptions.pack();
        winOptions.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        winOptions.setPosition(Gdx.graphics.getWidth()/4,Gdx.graphics.getWidth()/4);
        layer.addActor(winOptions);
        return layer;
    }

    public Table buildOptWinAudioSettings(){
        Table tbl = new Table();
        //添加标题audio
        tbl.pad(0,10,0,10);

//        BitmapFont font =new BitmapFont(Gdx.files.internal("menuscreen/winOptions.fnt"),Gdx.files.internal("menuscreen/winOptions.png"),false);
        Label audioLbl = new Label("Audio",new Label.LabelStyle(font,Color.BLACK));
        audioLbl.setFontScale(3*1.3f*width/1280f);
        tbl.add(audioLbl).colspan(3);
        tbl.row();
        tbl.columnDefaults(0).padRight(10);
        tbl.columnDefaults(1).padRight(10);
        //添加sound标签 声音滑动控件

        Label soundLbl = new Label("Sound",new Label.LabelStyle(font,Color.BLACK));
        soundLbl.setFontScale(3*1.3f*width/1280f);
        tbl.add(soundLbl).padTop(20*width/1280);
//        Texture sliderBac=new Texture("menuscreen/sliderbackground.png");
        TextureRegion sliderBac = AssetsController.instance.getRegion("sliderbackground");
        Image image = new Image(sliderBac);
        //TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(sliderBac);
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(new TextureRegionDrawable(sliderBac),
//                new TextureRegionDrawable(new Texture(Gdx.files.internal("menuscreen/sliderkuai.png"))));
                new TextureRegionDrawable(AssetsController.instance.getRegion("sliderkuai")));

        sldSound = new Slider(0.0f,1.0f,0.1f,false,sliderStyle);
        tbl.add(sldSound).width(3*sldSound.getWidth()*width/1280).padTop(20*width/1280);
        tbl.row();
        //添加music标签 音乐滑动控件
        Label musicLbl = new Label("Music",new Label.LabelStyle(font,Color.BLACK));
        tbl.add(musicLbl).padTop(20*width/1280);
        musicLbl.setFontScale(3*1.3f*width/1280);
        sldMusic = new Slider(0.0f,1.0f,0.1f,false,sliderStyle);
        tbl.add(sldMusic).width(3*sldMusic.getWidth()*width/1280).padTop(20*width/1280);
        tbl.row();
        return tbl;
    }

    public Table buildOptWinButtons(){
        Table tbl = new Table();

        //添加分割线
        Texture texture = new Texture(Gdx.files.internal("menuscreen/savecancelbutton.png"));
        //TextureRegion texture = AssetsController.instance.getRegion("savecancelbutton");
        font.getData().setScale(2*width/1280);
        //添加save按钮并且 初始化事件处理器
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(texture);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(textureRegionDrawable,textureRegionDrawable,textureRegionDrawable,font
        );


        btnWinOptSave = new TextButton("Save",textButtonStyle);
        tbl.add(btnWinOptSave).width(3*btnWinOptSave.getWidth()*width/1280f).height(3*btnWinOptSave.getHeight()*width/1280).padLeft(20*width/1280).padTop(20*width/1280);
        btnWinOptSave.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onSaveClicked();
                return false;
            }
        });
        // 添加cancel按钮并且 初始化事件处理器
        btnWinOptCancel = new TextButton("Cancel", textButtonStyle);
        tbl.add(btnWinOptCancel).width(3*btnWinOptSave.getWidth()*width/1280f).height(3*btnWinOptSave.getHeight()*width/1280).padLeft(20*width/1280).padTop(20*width/1280);
        btnWinOptCancel.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onCancelClicked();
                return false;
            }
        });
        return tbl;

    }

    public void onSaveClicked() {
        saveSettings();
        onCancelClicked();
    }

    public void onCancelClicked(){
        winOptions.setVisible(false);
        Gdx.input.setInputProcessor(getInputProcessor());
    }

    public void saveSettings(){
        DataController prefs = DataController.instance;
        prefs.setVolSound(sldSound.getValue());
        prefs.setVolMusic(sldMusic.getValue());
        prefs.saveSettings();
        winOptions.setVisible(false);
    }




    //自己英雄技能窗口
    public Table buildHeroInfoWindowLayer(){
        //font.getData().setScale(1.5f);
        Table layer = new Table();
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(AssetsController.instance.getRegion("textfieldbackground")));
        winHeroInfo = new Window("",windowStyle);
        //winOptions.setColor(1,1,1,1f);
        winHeroInfo.setVisible(false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.BLACK);
        Label label = new Label(description[myHeroTypeI()],labelStyle);
        label.setFontScale(1.0f);
        label.setSize(500,200);
        label.debug();
        winHeroInfo.addActor(label);
        //winOptions.pack();
        winHeroInfo.setSize(label.getWidth()*1.2f,label.getHeight()*1.2f);
        label.setPosition(label.getWidth()*0.1f,label.getHeight()*0.1f);
        winHeroInfo.setPosition(0,width/9+50);
        btnwinHInfoQuit = new Image(AssetsController.instance.getRegion("button_quit"));
        btnwinHInfoQuit.setScale(1.5f);
        btnwinHInfoQuit.setPosition(winHeroInfo.getWidth()-btnwinHInfoQuit.getWidth()*1.5f,winHeroInfo.getHeight()-btnwinHInfoQuit.getHeight()*1.5f);
        btnwinHInfoQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击他人英雄头像显示详细信息
                onWinHInfoQuitBottonClicked();
                return true;
            }
        });
        winHeroInfo.addActor(btnwinHInfoQuit);
        layer.addActor(winHeroInfo);
        return layer;
    }

    public void onWinHInfoQuitBottonClicked() {
        winHeroInfo.setVisible(false);
        Gdx.input.setInputProcessor(getInputProcessor());
    }




    //他人英雄技能窗口
    public Table buildOtherHeroInfoWindowLayer(){
        Table layer = new Table();
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(AssetsController.instance.getRegion("textfieldbackground")));
        winOtherHeroInfo = new Window("",windowStyle);
        winOtherHeroInfo.setSize(height/2,height/2);
        //winOptions.setColor(1,1,1,1f);
        //winOtherHeroInfo.addActor(buildWinOHInfoQuitBotton());
        winOtherHeroInfo.setVisible(false);

        //winOptions.pack();
        winOtherHeroInfo.setPosition(width-winOtherHeroInfo.getWidth(),imgMyHeroHead.getHeight()*width/15/imgMyHeroHead.getWidth());
        btnwinOHInfoQuit = new Image(AssetsController.instance.getRegion("button_quit"));
        btnwinHInfoQuit.setScale(1.5f);
        btnwinOHInfoQuit.setPosition(winOtherHeroInfo.getWidth()-btnwinOHInfoQuit.getWidth()*1.5f,winOtherHeroInfo.getHeight()-btnwinOHInfoQuit.getHeight()*1.5f);
        btnwinOHInfoQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击他人英雄头像显示详细信息
                onWinOHInfoQuitBottonClicked();
                return true;
            }
        });
        winOtherHeroInfo.addActor(btnwinOHInfoQuit);
        layer.addActor(winOtherHeroInfo);
        return layer;
    }


    public void onWinOHInfoQuitBottonClicked() {
        winOtherHeroInfo.setVisible(false);
        Gdx.input.setInputProcessor(getInputProcessor());
    }


    public void onHeroClicked(Player p){
        Gdx.app.log("qin","hero clicked is used in GameScreen");
        otherPlayer = p;
        isClickedHero = true;
        final int heroType;
        heroType = otherPlayer.getHeroType();
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
        imgOtherHeroHead = new Sprite(AssetsController.instance.getRegion(i+"_move0"));
        imgOtherHeroHead.setSize(43,43);
        imgOtherHeroHead.setScale(scale);
        imgOtherHeroHead.setPosition(width/15*12,0);
        //font.getData().setScale(1.0f);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font,font.getColor());
        Label label = new Label(description[heroType],labelStyle);
        label.setFontScale(1.0f);
        label.setSize(500,200);
        winOtherHeroInfo.addActor(label);
        winOtherHeroInfo.setSize(label.getWidth()*1.2f,label.getHeight()*1.2f);
        label.setPosition(label.getWidth()*0.1f,label.getHeight()*0.1f);
        winOtherHeroInfo.setPosition(width-600,width/9+50);
        winOtherHeroInfo.setVisible(false);
    }


    //结果窗口
    public void GameOver(){
        //游戏结算的弹窗
        TextureRegionDrawable winResultsDrawable = new TextureRegionDrawable(AssetsController.instance.getRegion("winresult"));
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),winResultsDrawable);
        winResults = new Window("",windowStyle);
        winResults.setSize(width/2,height/2);
        winResults.setPosition((width-winResults.getWidth())/2,(height-winResults.getHeight())/2);
        virtory = new Image(AssetsController.instance.getRegion("vitory"));
        failed = new Image(AssetsController.instance.getRegion("failed"));

        if((worldController.isGameOver()==1&&myPlayer().getTeam() == Constants.PLAYER.RED_TEAM)||(worldController.isGameOver()==2&&myPlayer().getTeam() == Constants.PLAYER.BLUE_TEAM)){
            virtory.setSize(winResults.getWidth()/3,winResults.getHeight()/3);
            virtory.setPosition((winResults.getWidth()-virtory.getWidth())/2,(winResults.getHeight()-virtory.getHeight())/1.25f);
            winResults.addActor(virtory);
            AudioController.instance.play(AssetsController.instance.win);
            Label goldReceiveLabel = new Label("获得100金币",new Label.LabelStyle(font, Color.BLACK));
            winResults.addActor(goldReceiveLabel);
            goldReceiveLabel.setSize(winResults.getWidth(),winResults.getHeight()/2);
            goldReceiveLabel.setAlignment(Align.center);
            goldReceiveLabel.setPosition(0,winResults.getHeight()/5);
            if(!isPlayed){
                AudioController.instance.play(AssetsController.instance.win);
                isPlayed =true;
            }
        }else{

            failed.setSize(winResults.getWidth()/3,winResults.getHeight()/3);
            failed.setPosition((winResults.getWidth()-failed.getWidth())/2,(winResults.getHeight()-failed.getHeight())/1.25f);
            winResults.addActor(failed);
            Label goldReceiveLabel = new Label("获得50金币",new Label.LabelStyle(font, Color.BLACK));
            winResults.addActor(goldReceiveLabel);
            goldReceiveLabel.setSize(winResults.getWidth(),winResults.getHeight()/2);
            goldReceiveLabel.setAlignment(Align.center);
            goldReceiveLabel.setPosition(0,winResults.getHeight()/5);
            if(!isPlayed){
                AudioController.instance.play(AssetsController.instance.lose);
                isPlayed = true;
            }
        }

        Image confirm = new Image(AssetsController.instance.getRegion("confirm"));
        confirm.setSize(winResults.getWidth()/3,winResults.getHeight()/3);
        confirm.setPosition((winResults.getWidth()-virtory.getWidth())/2,50);
        winResults.addActor(confirm);
        confirm.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //game.loadRoomScreen();
                winResults.setVisible(false);
                AudioController.instance.play(AssetsController.instance.btnClicked);
                return true;

            }
        });


    }


    //意外退出窗口
    public Table buildErrorQuitWindowLayer(){
        Table tbl = new Table();
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(AssetsController.instance.getRegion("winresult")));
        winErrorQuit = new Window("",windowStyle);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.BLACK);
        Label label = new Label("Because Manager is quitted,\n game error stop.",labelStyle);
        label.setFontScale(1.0f);
        winErrorQuit.setSize(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        winErrorQuit.setPosition(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4);
        label.setPosition(winErrorQuit.getWidth()/2-label.getPrefWidth()/2,2*winErrorQuit.getHeight()/3-label.getPrefHeight()/2);
        //winOptions.setColor(1,1,1,1f);
        winErrorQuit.addActor(buildErrorQuitWindowBotton());
        winErrorQuit.addActor(label);
        winErrorQuit.setVisible(false);
        tbl.addActor(winErrorQuit);
        //winOptions.pack();
        return tbl;
    }

    public Table buildErrorQuitWindowBotton(){
        Table layer = new Table();
        btnWinErrorQuit = new Image(AssetsController.instance.getRegion("confirm"));
        btnWinErrorQuit.setScale(0.6f);
        btnWinErrorQuit.setPosition(winErrorQuit.getWidth()/2-btnWinErrorQuit.getWidth()*0.6f/2,winErrorQuit.getHeight()/3-btnWinErrorQuit.getHeight()*0.6f/2);
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

    public void onErrorQuitBottonClicked() {
        winErrorQuit.setVisible(false);
        game.loadLobbyScreen();
    }


    //自己英雄信息
    public Player myPlayer(){
        int i;
        for(i=0;i<worldController.getPlayers().size;i++) {
            if (worldController.getPlayers().get(i).isMe()) {
                return worldController.getPlayers().get(i);
            }
        }
        return worldController.getPlayers().get(0);
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

    public int myHeroTypeI() {
        Gdx.app.log(TAG, "myPlayerHeroType=" + myPlayer().getHeroType());
        return myPlayer().getHeroType();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
    }

    public void playQuit(String ID){
        quitPlayer = ID;
        isquit = true;
    }

    @Override
    public void hide() {
        batch.dispose();
        stage.dispose();
        camera = null;
        cameraGUI = null;

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
        Gdx.input.setInputProcessor(stage);
        winErrorQuit.setVisible(true);
    }

    public void loadMenuScreen() {
        game.loadMenuScreen();
    }


    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        rebuildStage();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0x64/255.0f,0x95/255.0f,0xed/255.0f,0xff/255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        renderWorld(batch);
        Gdx.app.log("zc", "a frame");
        worldController.getWorld().getPlayerManager().update(deltaTime);
        worldController.testCollisions();
        renderGUI(batch);
        batch.end();
        stage.act();
        stage.draw();

        if(worldController.isGameOver()!= 0){
            GameOver();
        }
    }

    public InputProcessor getInputProcessor() {
        return new GestureDetector(inputController);
    }

    public boolean isClickedHero() {
        return isClickedHero;
    }

    public DirectedGame getGame(){
        return game;
    }
}