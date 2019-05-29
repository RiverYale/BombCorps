package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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



    //
    private int heroInfoType;
    private Image imgMyHeroHead;
    private Image imgOtherHeroHead;
    private Window winHeroInfo;
    private Image btnwinHInfoQuit;
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

    private void rebuildStage(){
        //build all layers
        Table layerQuitBottom = buildQuitBottonLayer();
        Table layerSkill = buildSkillLayer();
        Table layerMyHeroHead = buildHeroHeadLayer();
        Table layerHeroInfoWindow = buildHeroInfoWindowLayer();


        //assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerQuitBottom);
        stack.add(layerSkill);
        stack.add(layerMyHeroHead);
        stack.add(layerHeroInfoWindow);

    }

    private Table buildQuitBottonLayer(){
        Table layer = new Table();
        //+ quit botton
        btnQuit = new Image(new Texture(Gdx.files.internal("quit.png")));
        layer.add(btnQuit);
        btnQuit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //点击退出
                if(false/*是房主*/){

                }else {

                }
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
        winHeroInfo.add(buildWinHInfoQuitBotton());
        winHeroInfo.setVisible(false);

        //winOptions.pack();
        winHeroInfo.setPosition(Constants.VIEWPORT_GUI_WIDTH-winHeroInfo.getWidth()-50,50);
        return winHeroInfo;
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
        for(i=0;!worldController.getPlayers().get(i).isMe();i++){

        }
        return  worldController.getPlayers().get(i).getHeroType();
    }

    public Table onHeroClicked(Player p){
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
        return layer;
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
