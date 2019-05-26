package com.bombcorps.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.bombcorps.game.controller.CameraController;
import com.bombcorps.game.controller.InputController;

public class BombCorps extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	public int x = 0, y = 0, width = 4, height = 4;
    public OrthographicCamera camera;
    public CameraController cameraController;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

        cameraController = new CameraController();
        cameraController.setPosition(0, 0);
        camera = new OrthographicCamera(18, 10);
        Gdx.input.setInputProcessor(new GestureDetector(new InputController(cameraController, camera)));
	}

	@Override
	public void render () {
        cameraController.applyTo(camera);
        batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(1, 1, 1, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, x, y, width, height);
        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
