package com.bombcorps.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bombcorps.game.controller.WorldController;
import com.bombcorps.game.model.Constants;
import com.sun.media.sound.ModelDirectedPlayer;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class GameScreen extends AbstractGameScreen{
    private static final String TAG = GameScreen.class.getName();
    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private boolean paused;

    public GameScreen(Game game){
        super(game);
    }

    @Override
    public void render(float deltaTime) {
        //暂停时不更新
        if(!paused){
            //worldController.update(deltaTime);
        }
        Gdx.gl.glClearColor(0x64/255.0f,0x95/255.0f,0xed/255.0f,0xff/255.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldRenderer.render();
    }

    /*public InputProcessor getInputProcessor(){
        return (InputProcessor) worldController;
    }*/
    private Stage stage;
    private Skin skinBombCorps;

    private Image imgSkillOne;
    private Image imgSkillTwo;
    private Image imgSkillThree;
    private Image imgMovement;
    private Image imgAttrack;
    private Image imgEjection;
    private Image imgTurnEnd;

    private void rebuidStage(){
        //build all layers
        Table layerSkillOne = buildSkillOneLayer();
        Table layerSkillTwo = buildSkillTwoLayer();
        Table layerSkillThree = buildSkillThreeLayer();
        Table layerMovement = buildMovementLayer();
        Table layerAttrack = buildAtrrackLayer();
        Table layerEjection = buildEjectionLayer();
        Table layerTurnEnd = buildTurnEndLayer();
        //assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        //stack.setSize(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        stack.add(layerSkillOne);
        stack.add(layerSkillTwo);
        stack.add(layerSkillThree);
        stack.add(layerMovement);
        stack.add(layerAttrack);
        stack.add(layerEjection);
        stack.add(layerTurnEnd);
    }

    private Table buildTurnEndLayer(){
        Table layer = new Table();
        //+TurnEndLayer
        imgTurnEnd = new Image(skinBombCorps,"TurnEnd");
        layer.add(imgTurnEnd);
        return layer;
    }

    private Table buildSkillOneLayer(){
        Table layer = new Table();
        //+SkillOneLayer
        imgSkillOne = new Image(skinBombCorps,"SkillOne");
        layer.add(imgSkillOne);
        return layer;
    }

    private Table buildSkillTwoLayer(){
        Table layer = new Table();
        //+SkillTwoLayer
        imgSkillTwo = new Image(skinBombCorps,"SkillTwo");
        layer.add(imgSkillTwo);
        return layer;
    }

    private Table buildSkillThreeLayer(){
        Table layer = new Table();
        //+SkillOneLayer
        imgSkillThree = new Image(skinBombCorps,"SkillThree");
        layer.add(imgSkillThree);
        return layer;
    }

    private Table buildAtrrackLayer(){
        Table layer = new Table();
        //+AttrackLayer
        imgAttrack = new Image(skinBombCorps,"Attrack");
        layer.add(imgAttrack);
        return layer;
    }

    private Table buildEjectionLayer(){
        Table layer = new Table();
        //+EjectionLayer
        imgEjection = new Image(skinBombCorps,"Ejection");
        layer.add(imgEjection);
        return layer;
    }

    private Table buildMovementLayer(){
        Table layer = new Table();
        //+MovementLayer
        imgMovement = new Image(skinBombCorps,"Movement");
        layer.add(imgMovement);
        return layer;
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width,height);
    }

    @Override
    public void show() {
        worldController = new WorldController(/*game*/);
        worldRenderer = new WorldRenderer(worldController);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void hide() {
        worldRenderer.dispose();
        //worldController.dispose();
        Gdx.input.setCatchBackKey(false);
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
