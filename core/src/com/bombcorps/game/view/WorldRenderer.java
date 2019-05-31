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
    private SpriteBatch batch;
    private WorldController worldController;
    private ShaderProgram shaderMonpchrome;

    private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
    private Box2DDebugRenderer box2DDebugRenderer;

    public WorldRenderer(WorldController worldController){
        this.worldController = worldController;
        init();
    }

    private void init(){
        batch = new SpriteBatch();
        //camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH,Constants.VIEWPORT_HEIGHT);
        camera.position.set(0,0,0);
        camera.update();

        box2DDebugRenderer = new Box2DDebugRenderer();

        /*shaderMonpchrome = new ShaderProgram(Gdx.files.internal());
        if (!shaderMonochrome.isCompiled()) {
            String msg = "Could not compile shader program: " + shaderMonochrome.getLog();
            throw new GdxRuntimeException(msg);
        }
         */
    }

    public void render(){
        renderWorld(batch);
    }


    private void renderWorld(SpriteBatch batch){
       //worldController.cameraHelper.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        worldController.World().render(batch);
        batch.end();
    }

    public void resize(int width,int height){

    }

    public void dispose(){
        batch.dispose();
        //shaderMonochrome.dispose;
    }








}
