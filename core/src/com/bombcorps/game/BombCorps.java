package com.bombcorps.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.bombcorps.game.controller.InputController;

public class BombCorps extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	public static int x = 0, y = 0, width = 300, height = 300;
	public static float zoom = 1.0f;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		Gdx.input.setInputProcessor(new GestureDetector(new InputController()));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 1, 0.1f);
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
