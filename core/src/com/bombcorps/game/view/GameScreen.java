package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.bombcorps.game.controller.WorldController;
import com.bombcorps.game.model.Constants;
import com.bombcorps.game.model.Player;


/*
图片路径均非真正设置
 */
public class GameScreen extends AbstractGameScreen{
    private static final String TAG = GameScreen.class.getName();
    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private boolean paused;

    public GameScreen(DirectedGame game,WorldController worldController){
        super(game);
        this.worldController = worldController;
    }

    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0x64/255.0f,0x95/255.0f,0xed/255.0f,0xff/255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldRenderer.render();
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
    private Image btnwinHInfoQuit;
    //游戏异常退出弹窗
    private Window winErrorQuit;
    private Image btnWinErrorQuit;
    //
    private Image imgSkillOne;
    private Image imgSkillTwo;
    private Image imgSkillThree;
    private Image imgMovement;
    private Image imgAttrack;
    private Image imgEjection;
    private Image imgTurnEnd;
    //
    private Image btnQuit;
    private Image btnSettings;
    private Window winInGameSettings;

    private void rebuildStage(){
        //build all layers
        Table layerQuitBottom = buildBottonLayer();
        Table layerSkill = buildSkillLayer();
        Table layerMyHeroHead = buildHeroHeadLayer();
        Table layerHeroInfoWindow = buildHeroInfoWindowLayer();
        Table layerErrorQuitWindow = buildErrorQuitWindowLayer();
        Table layerMyHeroBasicInfoLabelLayer = buildMyHeroBasicInfoLableLayer();
        //assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerQuitBottom);
        stack.add(layerSkill);
        stack.add(layerMyHeroHead);
        stack.add(layerHeroInfoWindow);
        stack.add(layerErrorQuitWindow);
        stack.add(layerMyHeroBasicInfoLabelLayer);

    }


    private Table buildMyHeroBasicInfoLableLayer(){

        Table layer =new Table();
        BitmapFont font = new BitmapFont(Gdx.files.internal("winOptions.fnt"),Gdx.files.internal("winOptions.png"),false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        labelMyHeroBasicInfo = new Label(" ",labelStyle);
        layer.add(labelMyHeroBasicInfo);
        return layer;
    }

    private Table buildBottonLayer(){
        Table layer = new Table();
        //+ quit botton
        btnQuit = new Image(new Texture(Gdx.files.internal("quit.png")));
        layer.add(btnQuit);
        btnQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击退出
                if(false/*是房主*/){
                    onManagerQuitClicked();
                }else {
                    onQuitClicked();
                }
                return true;
            }
        });
        btnSettings = new Image(new Texture(Gdx.files.internal("Setting.png")));
        layer.add(btnSettings);
        btnSettings.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击设置
                winInGameSettings.setVisible(true);
                return true;
            }
        });
        return layer;
    }

    private Table buildErrorQuitWindowBotton(){
        Table layer = new Table();
        btnWinErrorQuit = new Image(new Texture(Gdx.files.internal("quit.png")));
        layer.add(btnWinErrorQuit);
        btnWinErrorQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击确认
                onErrorQuitClicked();
                return true;
            }
        });
        return layer;
    }


    private Table buildHeroHeadLayer(){
        Table layer = new Table();
        //+my hero's head appear
        imgMyHeroHead = new Image(new Texture(Gdx.files.internal("head.png")));
        layer.add(imgMyHeroHead);
        imgMyHeroHead.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击自己英雄头像显示详细信息
                heroInfoType = myHeroType();
                onHeroHeadClicked();
                return true;
            }
        });

        return layer;
    }



    private Table buildSkillLayer(){
        Table layer = new Table();

        //+TurnEndLayer
        imgTurnEnd = new Image(new Texture(Gdx.files.internal("TurnEnd.png")));
        layer.add(imgTurnEnd);
        imgTurnEnd.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击回合结束
                worldController.onOperationClicked(0);
                return true;
            }
        });


        //+SkillOneLayer
        imgSkillOne = new Image(new Texture(Gdx.files.internal("SkillOne.png")));
        layer.add(imgSkillOne);
        imgSkillOne.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击技能一
                worldController.onOperationClicked(0);
                return true;
            }
        });

        //+SkillTwoLayer
        imgSkillTwo = new Image(new Texture(Gdx.files.internal("SkillTwo.png")));
        layer.add(imgSkillTwo);
        imgSkillTwo.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击技能二
                worldController.onOperationClicked(0);
                return true;
            }
        });

        //+SkillThreeLayer
        imgSkillThree = new Image(new Texture(Gdx.files.internal("SkillThree.png")));
        layer.add(imgSkillThree);
        imgSkillThree.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击技能三
                worldController.onOperationClicked(0);
                return true;
            }
        });

        //+AttrackLayer
        imgAttrack = new Image(new Texture(Gdx.files.internal("Attrck.png")));
        layer.add(imgAttrack);
        imgAttrack.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击发射炸弹
                worldController.onOperationClicked(0);
                return true;
            }
        });

        //+EjectionLayer
        imgEjection = new Image(new Texture(Gdx.files.internal("Ejection.png")));
        layer.add(imgEjection);
        imgEjection.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击弹射
                worldController.onOperationClicked(0);
                return true;
            }
        });

        //+MovementLayer
        imgMovement = new Image(new Texture(Gdx.files.internal("Movement.png")));
        layer.add(imgMovement);
        imgMovement.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击发射炸弹
                worldController.onOperationClicked(0);
                return true;
            }
        });
        return layer;
    }

    private Table buildHeroInfoWindowLayer(){
        int heroInfoType;
        heroInfoType = this.heroInfoType;
        BitmapFont font =new BitmapFont(Gdx.files.internal("winHeroInfo.fnt"),Gdx.files.internal("winHeroInfo.png"),false);
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("window.png"))));
        winHeroInfo = new Window("heroInfo",windowStyle);
        winHeroInfo.setSize(300,300);
        //winOptions.setColor(1,1,1,1f);
        winHeroInfo.add(buildErrorQuitWindowBotton());
        winHeroInfo.setVisible(false);

        //winOptions.pack();
        winHeroInfo.setPosition(Constants.VIEWPORT_GUI_WIDTH-winHeroInfo.getWidth()-50,50);
        return winHeroInfo;
    }

    private Table buildErrorQuitWindowLayer(){
        BitmapFont font =new BitmapFont(Gdx.files.internal("winErrorQuit.fnt"),Gdx.files.internal("winErrorQuit.png"),false);
        Window.WindowStyle windowStyle = new Window.WindowStyle(font,font.getColor(),new TextureRegionDrawable(new Texture(Gdx.files.internal("window.png"))));
        winErrorQuit = new Window("ERROR QUIT",windowStyle);
        winErrorQuit.setSize(300,300);
        //winOptions.setColor(1,1,1,1f);
        winErrorQuit.add(buildWinHInfoQuitBotton());
        winErrorQuit.setVisible(false);

        //winOptions.pack();
        winErrorQuit.setPosition(Constants.VIEWPORT_GUI_WIDTH-winHeroInfo.getWidth()-50,50);
        return winErrorQuit;
    }

    private Table buildWinHInfoQuitBotton(){
        Table tbl = new Table();
        btnwinHInfoQuit = new Image(new Texture(Gdx.files.internal("quit.png")));

        tbl.add(btnwinHInfoQuit).padLeft(20);

        btnwinHInfoQuit.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
               onWinHInfoQuitBottonClicked();
           }
        });



       return tbl;
    }

    private void onWinHInfoQuitBottonClicked() {
        winHeroInfo.setVisible(false);
    }

    private void onHeroHeadClicked() {
        winHeroInfo.setVisible(true);
    }

    private int myHeroType(){
        int i;
        for(i=0;worldController.getPlayers().get(i).getState() != Constants.PLAYER.STATE_LOCAL;i++){

        }
        return  worldController.getPlayers().get(i).getHeroType();
    }

    private Table onHeroClicked(Player p){
        final int heroType;
        heroType = p.getHeroType();
        Table layer = new Table();
        imgOtherHeroHead = new Image(new Texture(Gdx.files.internal("head.png")));
        layer.add(imgOtherHeroHead);
        imgOtherHeroHead.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击他人英雄头像显示详细信息
                heroInfoType = heroType;
                onHeroHeadClicked();
                return true;
            }
        });

        BitmapFont font = new BitmapFont(Gdx.files.internal("winOptions.fnt"),Gdx.files.internal("winOptions.png"),false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        labelOtherHeroBasicInfo = new Label(" ",labelStyle);
        layer.add(labelOtherHeroBasicInfo);

        return layer;
    }

    private void onManagerQuitClicked() {
        //game.loadScreen
    }

    private void onQuitClicked() {
        //game.loadLobbyScreen();
    }

    private void GameOver(){

    }

    private void playQuit(String ID){
        BitmapFont font = new BitmapFont(Gdx.files.internal("winOptions.fnt"),Gdx.files.internal("winOptions.png"),false);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label label = new Label(ID+" has already quitted the game ,nmsl",labelStyle);
        stage.addActor(label);

    }

    private void onErrorQuitClicked() {
        winErrorQuit.setVisible(false);
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width,height);
    }

    @Override
    public void show() {
        worldRenderer = new WorldRenderer(worldController);
        stage = new Stage(new StretchViewport(
                Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        rebuildStage();
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
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


}
