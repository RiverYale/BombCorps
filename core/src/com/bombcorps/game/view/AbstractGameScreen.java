package com.bombcorps.game.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.bombcorps.game.controller.AssetsController;
import com.bombcorps.game.controller.WorldController;

public abstract class AbstractGameScreen implements Screen {
    protected DirectedGame game;

    public AbstractGameScreen(DirectedGame game) {
        this.game = game;
    }

    public abstract InputProcessor getInputProcessor();

    public abstract void render(float deltaTime);

    public abstract void resize(int width, int height);

    public abstract void show();

    public abstract void hide();

    public abstract void pause();

    public void resume() {
        //AssetsController.instance.init(new AssetManager());
    }

    public void dispose() {
        AssetsController.instance.dispose();

    }
}


