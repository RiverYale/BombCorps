package com.bombcorps.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.WorldController;
import com.bombcorps.game.model.Constants;

public class WorldRenderer {
    private OrthographicCamera camera;
    //private SpriteBatch batch;
    private WorldController worldController;

    public WorldRenderer(WorldController worldController){
        this.worldController = worldController;
        camera = worldController.getCamera();
        init();
    }

    private void init(){
        //batch = new SpriteBatch();
        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();
//        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        worldController.getCameraController().setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        camera.update();
    }

    public void render(SpriteBatch batch){
        renderWorld(batch);
    }


    private void renderWorld(SpriteBatch batch){
        worldController.getCameraController().applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.getWorld().render(batch);
        batch.end();
    }

    public void resize(int width,int height){
        camera.viewportWidth =(Constants.VIEWPORT_HEIGHT/height)*width;
        camera.update();
    }

    public void dispose(){
        //batch.dispose();
    }








}

