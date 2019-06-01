package com.bombcorps.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.CameraController;
import com.bombcorps.game.controller.InputController;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.view.DirectedGame;
import com.bombcorps.game.view.MenuScreen;
import com.bombcorps.game.view.ScreenTransition;

public class BombCorps extends DirectedGame {
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}
