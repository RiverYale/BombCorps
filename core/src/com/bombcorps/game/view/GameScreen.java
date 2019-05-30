package com.bombcorps.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

    public GameScreen(DirectedGame game){
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

    public InputProcessor getInputProcessor(){
        return (InputProcessor) worldController;
    }
    private Stage stage;

    private Image imgSkillOne;
    private Image imgSkillTwo;
    private Image imgSkillThree;
    private Image imgMovement;
    private Image imgAttrack;
    private Image imgEjection;
    private Image imgTurnEnd;

    private void rebuidStage(){
        //build all layers
        Table layerSkill = buildSkillLayer();
        //assemble stage for menu screen
        stage.clear();
        Stack stack = new Stack();
        stage.addActor(stack);
        stack.setSize(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        stack.add(layerSkill);

}

    private Table buildSkillLayer(){
        Table layer = new Table();

        //+TurnEndLayer
        imgTurnEnd = new Image(new Texture(Gdx.files.internal("TurnEnd.png")));
        layer.add(imgTurnEnd);


        //+SkillOneLayer
        imgSkillOne = new Image(new Texture(Gdx.files.internal("SkillOne.png")));
        layer.add(imgSkillOne);

        //+SkillTwoLayer
        imgSkillTwo = new Image(new Texture(Gdx.files.internal("SkillTwo.png")));
        layer.add(imgSkillTwo);

        //+SkillThreeLayer
        imgSkillThree = new Image(new Texture(Gdx.files.internal("SkillThree.png")));
        layer.add(imgSkillThree);

        //+AttrackLayer
        imgAttrack = new Image(new Texture(Gdx.files.internal("Attrck.png")));
        layer.add(imgAttrack);

        //+EjectionLayer
        imgEjection = new Image(new Texture(Gdx.files.internal("Ejection.png")));
        layer.add(imgEjection);

        //+MovementLayer
        imgMovement = new Image(new Texture(Gdx.files.internal("Movement.png")));
        layer.add(imgMovement);

        return layer;
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width,height);
    }

    @Override
    public void show() {
        //worldController = new WorldController(game);
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
