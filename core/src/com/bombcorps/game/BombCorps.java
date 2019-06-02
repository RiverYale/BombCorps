package com.bombcorps.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.CameraController;
import com.bombcorps.game.controller.InputController;
import com.bombcorps.game.controller.NetController;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.view.DirectedGame;

public class BombCorps extends DirectedGame {

	@Override
	public void create () {
		AssetsController.instance.loadTexture("menuscreen/menuscreen.txt");
		AssetsController.instance.loadTexture("lobbyscreen/lobbyscreen.txt");
		AssetsController.instance.loadTexture("roomscreen/roomscreen.txt");
		AssetsController.instance.loadTexture("infoscreen/infoscreen.txt");
		AssetsController.instance.loadTexture("gamescreen/gamescreen.txt");
		AssetsController.instance.loadTexture("hero/heros.txt");
		loadMenuScreen();
	}


}
